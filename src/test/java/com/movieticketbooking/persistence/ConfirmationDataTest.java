package com.movieticketbooking.persistence;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.SeatState;
import com.movieticketbooking.model.TestFixtures;
import com.movieticketbooking.repository.BookingRepository;
import com.movieticketbooking.repository.MovieRepository;
import com.movieticketbooking.repository.SeatRepository;
import com.movieticketbooking.repository.ShowRepository;
import com.movieticketbooking.repository.TheatreRepository;
import com.movieticketbooking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfirmationDataTest {

    @TempDir
    Path dir;

    private Database database;
    private BookingService bookingService;
    private BookingRepository bookings;
    private MovieRepository movies;
    private TheatreRepository theatres;
    private ShowRepository shows;
    private SeatRepository seats;

    @BeforeEach
    void setUp() {
        database = new Database(dir.resolve("confirmation.db").toString());
        database.init();
        bookingService = new BookingService(database);
        bookings = new BookingRepository(database);
        movies = new MovieRepository();
        theatres = new TheatreRepository();
        shows = new ShowRepository(movies, theatres);
        seats = new SeatRepository();
    }

    @Test
    void confirmationDataMatchesSavedBookingAcrossRestart() {
        Booking confirmed = bookingService.confirmBooking(
                TestFixtures.customer(), loadShow(1L), List.of(openSeat(1L, 0), openSeat(1L, 1)));
        String code = confirmed.getBookingCode();

        Database restarted = new Database(database.getPath());
        restarted.init();
        Booking saved = new BookingRepository(restarted).findByBookingCode(code).orElseThrow();

        assertEquals(confirmed.getBookingCode(), saved.getBookingCode());
        assertEquals(confirmed.getId(), saved.getId());
        assertEquals("Alice Johnson", saved.getCustomer().getFullName());
        assertEquals("alice@example.com", saved.getCustomer().getEmail());
        assertEquals(confirmed.getShow().getMovie().getTitle(), saved.getShow().getMovie().getTitle());
        assertEquals(confirmed.getShow().getTheatre().getName(), saved.getShow().getTheatre().getName());
        assertEquals(List.of("A1", "A2"), saved.getSeats().stream().map(Seat::getLabel).toList());
        assertEquals(2, saved.getTicket().getQuantity());
        assertEquals(confirmed.getTicket().getUnitPrice(), saved.getTicket().getUnitPrice());
        assertEquals(confirmed.getTicket().getTotal(), saved.getTicket().getTotal());
        assertTrue(saved.getBookingCode().startsWith("BK-"));
    }

    @Test
    void newBookingCanStartCleanlyAfterReopeningFreshGrid() {
        bookingService.confirmBooking(TestFixtures.customer(), loadShow(1L),
                List.of(openSeat(1L, 0), openSeat(1L, 1)));

        Booking next = bookingService.confirmBooking(TestFixtures.customer(), loadShow(1L),
                List.of(openSeat(1L, 2)));

        assertEquals(2, bookings.findAll().size());
        assertTrue(next.getBookingCode().startsWith("BK-"));
        assertEquals(SeatState.BOOKED, currentSeat(1L, 0).getState());
        assertEquals(SeatState.BOOKED, currentSeat(1L, 2).getState());
    }

    private com.movieticketbooking.model.Show loadShow(long showId) {
        try (Connection connection = database.open()) {
            var base = shows.findById(connection, showId).orElseThrow();
            return new com.movieticketbooking.model.Show(base.getId(), base.getMovie(), base.getTheatre(),
                    base.getStartTime(), base.getEndTime(), seats.findByShow(connection, showId));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Seat openSeat(long showId, int index) {
        return loadShow(showId).getSeats().get(index);
    }

    private Seat currentSeat(long showId, int index) {
        try (Connection connection = database.open()) {
            return seats.findByShow(connection, showId).get(index);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}