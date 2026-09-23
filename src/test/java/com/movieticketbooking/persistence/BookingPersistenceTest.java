package com.movieticketbooking.persistence;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.SeatState;
import com.movieticketbooking.model.TestFixtures;
import com.movieticketbooking.model.Ticket;
import com.movieticketbooking.model.pricing.StandardPricing;
import com.movieticketbooking.repository.BookingRepository;
import com.movieticketbooking.repository.MovieRepository;
import com.movieticketbooking.repository.SeatRepository;
import com.movieticketbooking.repository.ShowRepository;
import com.movieticketbooking.repository.TheatreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingPersistenceTest {

    @TempDir
    Path dir;

    private Database database;
    private BookingRepository bookings;
    private MovieRepository movies;
    private TheatreRepository theatres;
    private ShowRepository shows;
    private SeatRepository seats;

    @BeforeEach
    void setUp() {
        database = new Database(dir.resolve("booking.db").toString());
        database.init();
        bookings = new BookingRepository(database);
        movies = new MovieRepository();
        theatres = new TheatreRepository();
        shows = new ShowRepository(movies, theatres);
        seats = new SeatRepository();
    }

    @Test
    void createPersistsAndReturnsBookingWithCode() {
        Booking draft = buildBooking(1L, 2);

        Booking created = bookings.create(draft);

        assertTrue(created.getBookingCode().startsWith("BK-"));
        assertTrue(created.isConfirmed());
    }

    @Test
    void createdBookingIsRetrievableWithDetails() {
        Booking created = bookings.create(buildBooking(1L, 2));

        Booking loaded = bookings.findByBookingCode(created.getBookingCode()).orElseThrow();

        assertEquals(created.getBookingCode(), loaded.getBookingCode());
        assertEquals(new BigDecimal("24.00"), loaded.getTicket().getTotal());
        assertEquals(2, loaded.getTicket().getQuantity());
        assertEquals("Alice Johnson", loaded.getCustomer().getFullName());
        assertEquals(1L, loaded.getShow().getId());
        assertEquals(List.of("A1", "A2"), loaded.getSeats().stream().map(Seat::getLabel).toList());
    }

    @Test
    void duplicateSeatCannotBeBookedTwiceForSameShow() {
        bookings.create(buildBooking(1L, 2));

        Booking duplicate = buildBooking(1L, 2);

        assertThrows(IllegalStateException.class, () -> bookings.create(duplicate));
        assertEquals(1, bookings.findAll().size(), "failed booking must not leave a partial record");
    }

    @Test
    void samePositionOnDifferentShowsIsAllowed() {
        Booking first = bookings.create(buildBooking(1L, 2));
        Booking second = bookings.create(buildBooking(2L, 2));

        assertEquals(2, bookings.findAll().size());
        assertEquals("BK-", first.getBookingCode().substring(0, 3));
        assertEquals("BK-", second.getBookingCode().substring(0, 3));
    }

    @Test
    void dataSurvivesRestart() {
        Booking created = bookings.create(buildBooking(1L, 3));

        Database restarted = new Database(database.getPath());
        restarted.init();

        BookingRepository reopened = new BookingRepository(restarted);
        Booking loaded = reopened.findByBookingCode(created.getBookingCode()).orElseThrow();
        assertEquals(new BigDecimal("36.00"), loaded.getTicket().getTotal());
        assertEquals(3, loaded.getSeats().size());

        try (Connection connection = restarted.open()) {
            Seat seat = seats.findByShow(connection, 1L).get(0);
            assertEquals(SeatState.BOOKED, seat.getState());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Booking buildBooking(long showId, int seatCount) {
        try (Connection connection = database.open()) {
            Show base = shows.findById(connection, showId).orElseThrow();
            Show full = new Show(base.getId(), base.getMovie(), base.getTheatre(),
                    base.getStartTime(), base.getEndTime(), seats.findByShow(connection, showId));
            List<Seat> chosen = full.getSeats().subList(0, seatCount);
            Ticket ticket = new Ticket(full, chosen, full.getMovie().getTicketPrice(), new StandardPricing());
            return new Booking(0L, TestFixtures.customer(), full, chosen, ticket);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}