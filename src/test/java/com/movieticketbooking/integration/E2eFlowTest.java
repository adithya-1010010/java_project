package com.movieticketbooking.integration;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Movie;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.SeatState;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.TestFixtures;
import com.movieticketbooking.repository.BookingRepository;
import com.movieticketbooking.repository.SeatRepository;
import com.movieticketbooking.service.AuthenticationService;
import com.movieticketbooking.service.BookingService;
import com.movieticketbooking.service.MovieService;
import com.movieticketbooking.service.SeatService;
import com.movieticketbooking.service.ShowService;
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

class E2eFlowTest {

    @TempDir
    Path dir;

    private Database database;
    private AuthenticationService auth;
    private MovieService movies;
    private ShowService showsForMovie;
    private SeatService seats;
    private BookingService bookingService;
    private BookingRepository bookings;
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() {
        database = new Database(dir.resolve("e2e.db").toString());
        database.init();
        auth = new AuthenticationService(database);
        movies = new MovieService(database);
        showsForMovie = new ShowService(database);
        seats = new SeatService(database);
        bookingService = new BookingService(database);
        bookings = new BookingRepository(database);
        seatRepository = new SeatRepository();
    }

    @Test
    void happyPathLoginBrowseSelectBookConfirm() {
        assertTrue(auth.authenticate("demo", "demo123").isPresent(), "demo can log in");

        List<Movie> catalog = movies.listMovies();
        assertTrue(catalog.size() >= 4, "catalog is seeded");
        Movie movie = catalog.get(0);

        List<Show> showOptions = showsForMovie.showsForMovie(movie.getId());
        assertTrue(!showOptions.isEmpty(), "movie has shows");
        assertTrue(showOptions.stream().allMatch(show -> show.getMovie().getId() == movie.getId()));

        Show show = fullShow(showOptions.get(0));
        List<Seat> grid = seats.seatsForShow(show.getId());
        assertEquals(80, grid.size(), "row/column grid is 80 seats");
        List<Seat> selection = grid.subList(0, 2);

        Booking booking = bookingService.confirmBooking(TestFixtures.customer(), show, selection);

        assertTrue(booking.getBookingCode().startsWith("BK-"));
        assertTrue(booking.isConfirmed());
        assertEquals(2, booking.getTicket().getQuantity());
        BigDecimal expected = movie.getTicketPrice().multiply(BigDecimal.valueOf(2)).setScale(2);
        assertEquals(expected, booking.getTicket().getTotal());
    }

    @Test
    void invalidLoginNeverEntersApplication() {
        assertTrue(auth.authenticate("demo", "wrong").isEmpty());
        assertTrue(auth.authenticate("demo", "").isEmpty());
        assertTrue(auth.authenticate("unknown", "demo123").isEmpty());
    }

    @Test
    void invalidBookingInputsAreRejected() {
        Show show = fullShow(showsForMovie.showsForMovie(movies.listMovies().get(0).getId()).get(0));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(null, show, seats.seatsForShow(show.getId()).subList(0, 1)));
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(TestFixtures.customer(), show, List.of()));
        assertEquals(0, bookings.findAll().size());
    }

    @Test
    void duplicateSeatBookingIsPreventedAcrossSessions() {
        Show show = fullShow(showsForMovie.showsForMovie(movies.listMovies().get(0).getId()).get(0));
        Seat contested = seats.seatsForShow(show.getId()).get(0);
        Booking first = bookingService.confirmBooking(TestFixtures.customer(), show, List.of(contested));

        assertThrows(IllegalStateException.class,
                () -> bookingService.confirmBooking(TestFixtures.customer(), fullShow(show), List.of(contested)));

        assertEquals(1, bookings.findAll().size());
        assertTrue(bookings.findByBookingCode(first.getBookingCode()).isPresent());
    }

    @Test
    void bookingSurvivesRestartAndStillBlocksDuplicateSeats() {
        Show show = fullShow(showsForMovie.showsForMovie(movies.listMovies().get(0).getId()).get(0));
        Booking first = bookingService.confirmBooking(TestFixtures.customer(), show,
                seats.seatsForShow(show.getId()).subList(0, 2));

        Database restarted = new Database(database.getPath());
        restarted.init();
        Booking saved = new BookingRepository(restarted).findByBookingCode(first.getBookingCode()).orElseThrow();
        assertEquals(first.getTicket().getTotal(), saved.getTicket().getTotal());

        try (Connection connection = restarted.open()) {
            List<Seat> reloaded = seatRepository.findByShow(connection, show.getId());
            assertEquals(SeatState.BOOKED, reloaded.get(0).getState());
            assertEquals(SeatState.AVAILABLE, reloaded.get(2).getState());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        BookingService restartedService = new BookingService(restarted);
        assertThrows(IllegalStateException.class,
                () -> restartedService.confirmBooking(TestFixtures.customer(), saved.getShow(),
                        List.of(saved.getSeats().get(0))));
        assertEquals(1, new BookingRepository(restarted).findAll().size());
    }

    @Test
    void emptyStatesAreHandled() {
        assertTrue(showsForMovie.showsForMovie(999L).isEmpty());
        assertTrue(seats.seatsForShow(999L).isEmpty());
        assertTrue(movies.findById(999L).isEmpty());
    }

    private Show fullShow(Show show) {
        List<Seat> grid = seats.seatsForShow(show.getId());
        return new Show(show.getId(), show.getMovie(), show.getTheatre(),
                show.getStartTime(), show.getEndTime(), grid);
    }
}