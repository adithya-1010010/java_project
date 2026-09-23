package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Seat;
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

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeatServiceTest {

    @TempDir
    Path dir;

    private Database database;
    private SeatService seatService;
    private SeatRepository seatRepository;
    private ShowRepository shows;
    private BookingRepository bookings;

    @BeforeEach
    void setUp() {
        database = new Database(dir.resolve("seats.db").toString());
        database.init();
        seatService = new SeatService(database);
        seatRepository = new SeatRepository();
        shows = new ShowRepository(new MovieRepository(), new TheatreRepository());
        bookings = new BookingRepository(database);
    }

    @Test
    void loadsFullLayoutForSeededShow() {
        List<Seat> seats = seatService.seatsForShow(1L);

        assertEquals(80, seats.size());
        assertEquals('A', seats.get(0).getRow());
        assertEquals(1, seats.get(0).getColumn());
        assertEquals('H', seats.get(79).getRow());
        assertEquals(10, seats.get(79).getColumn());
    }

    @Test
    void seatsStartAvailable() {
        assertTrue(seatService.seatsForShow(1L).stream().allMatch(Seat::isAvailable));
    }

    @Test
    void bookedSeatsReflectDatabaseState() {
        bookFirstTwoSeats();

        List<Seat> reloaded = seatService.seatsForShow(1L);
        assertEquals(SeatState.BOOKED, reloaded.get(0).getState());
        assertEquals(SeatState.BOOKED, reloaded.get(1).getState());
        assertEquals(SeatState.AVAILABLE, reloaded.get(2).getState());
    }

    @Test
    void unknownShowHasNoSeats() {
        assertTrue(seatService.seatsForShow(999L).isEmpty());
    }

    private void bookFirstTwoSeats() {
        try (Connection connection = database.open()) {
            var base = shows.findById(connection, 1L).orElseThrow();
            var full = new com.movieticketbooking.model.Show(base.getId(), base.getMovie(), base.getTheatre(),
                    base.getStartTime(), base.getEndTime(), seatRepository.findByShow(connection, 1L));
            List<Seat> chosen = full.getSeats().subList(0, 2);
            Ticket ticket = new Ticket(full, chosen, full.getMovie().getTicketPrice(), new StandardPricing());
            bookings.create(new Booking(0L, TestFixtures.customer(), full, chosen, ticket));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}