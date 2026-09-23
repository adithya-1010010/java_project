package com.movieticketbooking.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShowTest {

    @Test
    void createsShowWithSeats() {
        List<Seat> seats = TestFixtures.seats(2, 3);
        Show show = new Show(
                1L,
                TestFixtures.movie(),
                TestFixtures.theatre(),
                LocalDateTime.of(2026, 10, 1, 18, 0),
                LocalDateTime.of(2026, 10, 1, 20, 30),
                seats);

        assertEquals(6, show.getSeats().size());
        assertTrue(show.hasSeat(seats.get(0)));
        assertFalse(show.hasSeat(new Seat(999L, 'Z', 9)));
    }

    @Test
    void rejectsNullMovieOrTheatre() {
        LocalDateTime start = LocalDateTime.of(2026, 10, 1, 18, 0);
        LocalDateTime end = start.plusHours(2);
        List<Seat> seats = TestFixtures.seats(1, 2);

        assertThrows(IllegalArgumentException.class,
                () -> new Show(1L, null, TestFixtures.theatre(), start, end, seats));
        assertThrows(IllegalArgumentException.class,
                () -> new Show(1L, TestFixtures.movie(), null, start, end, seats));
    }

    @Test
    void rejectsEndBeforeStart() {
        LocalDateTime start = LocalDateTime.of(2026, 10, 1, 18, 0);
        assertThrows(IllegalArgumentException.class,
                () -> new Show(1L, TestFixtures.movie(), TestFixtures.theatre(), start, start.minusMinutes(1),
                        TestFixtures.seats(1, 2)));
    }

    @Test
    void rejectsDuplicateSeatsInLayout() {
        Seat duplicate = new Seat(1L, 'A', 1);
        assertThrows(IllegalArgumentException.class,
                () -> new Show(1L, TestFixtures.movie(), TestFixtures.theatre(),
                        LocalDateTime.of(2026, 10, 1, 18, 0),
                        LocalDateTime.of(2026, 10, 1, 20, 30),
                        List.of(duplicate, duplicate)));
    }

    @Test
    void availableSeatsExcludeBooked() {
        List<Seat> seats = TestFixtures.seats(1, 3);
        Show show = TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), seats);

        seats.get(0).book();

        assertEquals(2, show.getAvailableSeats().size());
        assertFalse(show.getAvailableSeats().contains(seats.get(0)));
    }
}