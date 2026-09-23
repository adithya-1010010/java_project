package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Customer;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.SeatState;
import com.movieticketbooking.model.Show;
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

class BookingServiceTest {

    @TempDir
    Path dir;

    private Database database;
    private BookingService bookingService;
    private BookingRepository bookings;
    private MovieRepository movies;
    private TheatreRepository theatres;
    private ShowRepository shows;
    private SeatRepository seats;
    private Customer customer;

    @BeforeEach
    void setUp() {
        database = new Database(dir.resolve("booking-service.db").toString());
        database.init();
        bookingService = new BookingService(database);
        bookings = new BookingRepository(database);
        movies = new MovieRepository();
        theatres = new TheatreRepository();
        shows = new ShowRepository(movies, theatres);
        seats = new SeatRepository();
        customer = TestFixtures.customer();
    }

    @Test
    void singleTicketBookingIsPersistedWithCode() {
        Show show = loadShow(1L);
        List<Seat> selection = List.of(show.getSeats().get(0));

        Booking booking = bookingService.confirmBooking(customer, show, selection);

        assertTrue(booking.getBookingCode().startsWith("BK-"));
        assertTrue(booking.isConfirmed());
        assertEquals(1, booking.getTicket().getQuantity());
        assertEquals(1, bookings.findAll().size());
        assertEquals(SeatState.BOOKED, loadShow(1L).getSeats().get(0).getState());
    }

    @Test
    void multipleTicketBookingStoresAllSeats() {
        Show show = loadShow(1L);
        List<Seat> selection = show.getSeats().subList(0, 3);

        Booking booking = bookingService.confirmBooking(customer, show, selection);

        assertEquals(3, booking.getTicket().getQuantity());
        assertEquals(List.of("A1", "A2", "A3"),
                booking.getSeats().stream().map(Seat::getLabel).toList());
        assertTrue(loadShow(1L).getSeats().subList(0, 3).stream().noneMatch(Seat::isAvailable));
    }

    @Test
    void totalCostIsUnitPriceTimesQuantity() {
        Show show = loadShow(1L);
        List<Seat> selection = show.getSeats().subList(0, 3);

        Booking booking = bookingService.confirmBooking(customer, show, selection);

        BigDecimal unit = show.getMovie().getTicketPrice();
        assertEquals(unit.multiply(BigDecimal.valueOf(3)).setScale(2), booking.getTicket().getTotal());
        assertEquals(3, booking.getTicket().getQuantity());
        assertEquals(unit, booking.getTicket().getUnitPrice());
    }

    @Test
    void missingCustomerDetailsAreRejected() {
        Show show = loadShow(1L);
        List<Seat> selection = List.of(show.getSeats().get(0));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(null, show, selection));
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(new Customer("", "alice@example.com", "1"), show, selection));
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(new Customer("Alice", "not-an-email", "1"), show, selection));
        assertEquals(0, bookings.findAll().size());
    }

    @Test
    void noSelectedSeatsAreRejected() {
        Show show = loadShow(1L);

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(customer, show, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(customer, show, null));
        assertEquals(0, bookings.findAll().size());
    }

    @Test
    void seatFromAnotherShowIsRejected() {
        Show first = loadShow(1L);
        Show second = loadShow(2L);
        Seat foreign = second.getSeats().get(0);

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.confirmBooking(customer, first, List.of(foreign)));
        assertEquals(0, bookings.findAll().size());
    }

    @Test
    void alreadyBookedSeatFailsAtServiceCheck() {
        Show show = loadShow(1L);
        Seat contested = show.getSeats().get(0);
        bookingService.confirmBooking(customer, show, List.of(contested));

        Show secondAttempt = loadShow(1L);
        Seat stillSelected = secondAttempt.getSeats().get(0);

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> bookingService.confirmBooking(customer, secondAttempt, List.of(stillSelected)));
        assertEquals(BookingService.SEATS_UNAVAILABLE, failure.getMessage());
        assertEquals(1, bookings.findAll().size(), "failed booking must not create a record");
    }

    @Test
    void staleSeatLosesRaceAtFinalPersistenceLayer() {
        Show staleShow = loadShow(1L);
        Seat staleSeat = staleShow.getSeats().get(0);

        bookingService.confirmBooking(customer, loadShow(1L), List.of(staleSeat));

        Ticket ticket = new Ticket(staleShow, List.of(staleSeat),
                staleShow.getMovie().getTicketPrice(), new StandardPricing());
        Booking staleDraft = new Booking(0L, customer, staleShow, List.of(staleSeat), ticket);
        staleDraft.confirm();

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> bookings.create(staleDraft));
        assertTrue(failure.getMessage().contains("no longer available")
                || failure.getMessage().contains("could not be persisted"));
        assertEquals(1, bookings.findAll().size(), "failed booking must not leave a partial record");
        assertEquals(SeatState.BOOKED, loadShow(1L).getSeats().get(0).getState());
    }

    private Show loadShow(long showId) {
        try (Connection connection = database.open()) {
            Show base = shows.findById(connection, showId).orElseThrow();
            return new Show(base.getId(), base.getMovie(), base.getTheatre(),
                    base.getStartTime(), base.getEndTime(), seats.findByShow(connection, showId));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}