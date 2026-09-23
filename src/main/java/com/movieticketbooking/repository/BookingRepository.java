package com.movieticketbooking.repository;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Customer;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.Ticket;
import com.movieticketbooking.util.Money;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingRepository {

    private static final String SEAT_CONFLICT_MARKER = "booking_seats.seat_id";

    private final Database database;
    private final CustomerRepository customers;
    private final MovieRepository movies;
    private final TheatreRepository theatres;
    private final ShowRepository shows;
    private final SeatRepository seats;

    public BookingRepository(Database database) {
        this.database = database;
        this.customers = new CustomerRepository();
        this.movies = new MovieRepository();
        this.theatres = new TheatreRepository();
        this.shows = new ShowRepository(movies, theatres);
        this.seats = new SeatRepository();
    }

    public Booking create(Booking booking) {
        try (Connection connection = database.open()) {
            connection.setAutoCommit(false);
            try {
                long customerId = customers.insert(connection, booking.getCustomer());
                String code = "BK-" + System.currentTimeMillis();
                long bookingId = insertBooking(connection, code, customerId, booking);
                for (Seat seat : booking.getSeats()) {
                    insertBookingSeat(connection, bookingId, seat);
                }
                seats.markBooked(connection, booking.getSeats());
                connection.commit();
                return findByBookingCode(connection, code)
                        .orElseThrow(() -> new IllegalStateException("booking was not persisted"));
            } catch (SQLException e) {
                connection.rollback();
                throw translate(e);
            }
        } catch (SQLException e) {
            throw translate(e);
        }
    }

    public Optional<Booking> findByBookingCode(String code) {
        try (Connection connection = database.open()) {
            return findByBookingCode(connection, code);
        } catch (SQLException e) {
            throw new IllegalStateException("could not read booking", e);
        }
    }

    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        try (Connection connection = database.open();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT id FROM bookings ORDER BY created_at, id")) {
            ResultSet rows = statement.executeQuery();
            while (rows.next()) {
                bookings.add(findById(connection, rows.getLong("id")).orElseThrow());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("could not read bookings", e);
        }
        return bookings;
    }

    private Optional<Booking> findByBookingCode(Connection connection, String code) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, booking_code, customer_id, show_id, total_cents, quantity, created_at "
                        + "FROM bookings WHERE booking_code = ?")) {
            statement.setString(1, code);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return Optional.of(map(connection, rows));
            }
        }
        return Optional.empty();
    }

    private Optional<Booking> findById(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, booking_code, customer_id, show_id, total_cents, quantity, created_at "
                        + "FROM bookings WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return Optional.of(map(connection, rows));
            }
        }
        return Optional.empty();
    }

    private Booking map(Connection connection, ResultSet rows) throws SQLException {
        long bookingId = rows.getLong("id");
        String code = rows.getString("booking_code");
        Customer customer = customers.findById(connection, rows.getLong("customer_id")).orElseThrow();
        Show show = loadShowWithSeats(connection, rows.getLong("show_id"));

        List<Long> bookedSeatIds = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT seat_id FROM booking_seats WHERE booking_id = ?")) {
            statement.setLong(1, bookingId);
            ResultSet seats = statement.executeQuery();
            while (seats.next()) {
                bookedSeatIds.add(seats.getLong("seat_id"));
            }
        }

        List<Seat> booked = new ArrayList<>();
        for (Seat seat : show.getSeats()) {
            if (bookedSeatIds.contains(seat.getId())) {
                booked.add(seat);
            }
        }

        BigDecimal total = Money.fromCents(rows.getLong("total_cents"));
        Ticket ticket = Ticket.reconstruct(show, booked, show.getMovie().getTicketPrice(), total);
        return Booking.reconstruct(
                bookingId,
                customer,
                show,
                booked,
                ticket,
                code,
                LocalDateTime.parse(rows.getString("created_at")));
    }

    private Show loadShowWithSeats(Connection connection, long showId) throws SQLException {
        Show base = shows.findById(connection, showId).orElseThrow();
        return new Show(
                base.getId(),
                base.getMovie(),
                base.getTheatre(),
                base.getStartTime(),
                base.getEndTime(),
                seats.findByShow(connection, showId));
    }

    private long insertBooking(Connection connection, String code, long customerId, Booking booking) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO bookings (booking_code, customer_id, show_id, total_cents, quantity, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, code);
            statement.setLong(2, customerId);
            statement.setLong(3, booking.getShow().getId());
            statement.setLong(4, Money.toCents(booking.getTicket().getTotal()));
            statement.setInt(5, booking.getTicket().getQuantity());
            statement.setString(6, booking.getCreatedAt().toString());
            statement.executeUpdate();
        }
        return lastInsertId(connection);
    }

    private void insertBookingSeat(Connection connection, long bookingId, Seat seat) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO booking_seats (booking_id, seat_id) VALUES (?, ?)")) {
            statement.setLong(1, bookingId);
            statement.setLong(2, seat.getId());
            statement.executeUpdate();
        }
    }

    private long lastInsertId(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT last_insert_rowid()")) {
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return rows.getLong(1);
            }
            throw new SQLException("no id returned after insert");
        }
    }

    private IllegalStateException translate(SQLException e) {
        if (e.getMessage() != null && e.getMessage().contains(SEAT_CONFLICT_MARKER)) {
            return new IllegalStateException("one or more seats are no longer available", e);
        }
        return new IllegalStateException("booking could not be persisted", e);
    }
}