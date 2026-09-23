package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Customer;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.Ticket;
import com.movieticketbooking.model.pricing.StandardPricing;
import com.movieticketbooking.repository.BookingRepository;
import com.movieticketbooking.repository.SeatRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingService {

    public static final String SEATS_UNAVAILABLE = "one or more selected seats are no longer available";

    private final Database database;
    private final BookingRepository bookings;
    private final SeatRepository seats;

    public BookingService(Database database) {
        this.database = database;
        this.bookings = new BookingRepository(database);
        this.seats = new SeatRepository();
    }

    public Booking confirmBooking(Customer customer, Show show, List<Seat> selectedSeats) {
        requireCustomer(customer);
        requireShow(show);
        requireSelection(selectedSeats);

        List<Seat> fresh = refreshAndVerifyAvailability(show.getId(), selectedSeats);
        Show refreshedShow = withSeats(show, fresh);

        Ticket ticket = new Ticket(
                refreshedShow,
                fresh,
                refreshedShow.getMovie().getTicketPrice(),
                new StandardPricing());

        Booking booking = new Booking(0L, customer, refreshedShow, fresh, ticket);
        booking.confirm();
        return bookings.create(booking);
    }

    private void requireCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("customer details are required");
        }
    }

    private void requireShow(Show show) {
        if (show == null) {
            throw new IllegalArgumentException("show is required");
        }
    }

    private void requireSelection(List<Seat> selectedSeats) {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            throw new IllegalArgumentException("at least one seat is required");
        }
    }

    private List<Seat> refreshAndVerifyAvailability(long showId, List<Seat> selectedSeats) {
        try (Connection connection = database.open()) {
            List<Seat> current = seats.findByShow(connection, showId);
            List<Seat> fresh = new ArrayList<>();
            for (Seat selected : selectedSeats) {
                Seat matching = findById(current, selected.getId());
                if (matching == null) {
                    throw new IllegalArgumentException("seat does not belong to show: " + selected.getLabel());
                }
                if (!matching.isAvailable()) {
                    throw new IllegalStateException(SEATS_UNAVAILABLE);
                }
                fresh.add(matching);
            }
            return fresh;
        } catch (SQLException e) {
            throw new IllegalStateException("could not verify seat availability", e);
        }
    }

    private Seat findById(List<Seat> seats, long id) {
        return seats.stream().filter(seat -> seat.getId() == id).findFirst().orElse(null);
    }

    private Show withSeats(Show show, List<Seat> freshSeats) {
        return new Show(show.getId(), show.getMovie(), show.getTheatre(),
                show.getStartTime(), show.getEndTime(), freshSeats);
    }
}