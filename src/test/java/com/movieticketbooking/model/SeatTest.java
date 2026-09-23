package com.movieticketbooking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeatTest {

    @Test
    void createsSeatAvailableByDefault() {
        Seat seat = new Seat(1L, 'A', 3);

        assertEquals('A', seat.getRow());
        assertEquals(3, seat.getColumn());
        assertEquals("A3", seat.getLabel());
        assertEquals(SeatState.AVAILABLE, seat.getState());
        assertTrue(seat.isAvailable());
    }

    @Test
    void rejectsNonLetterRow() {
        assertThrows(IllegalArgumentException.class, () -> new Seat(1L, '3', 3));
    }

    @Test
    void rejectsNonPositiveColumn() {
        assertThrows(IllegalArgumentException.class, () -> new Seat(1L, 'A', 0));
    }

    @Test
    void bookMarksSeatBookedOnce() {
        Seat seat = new Seat(1L, 'A', 3);

        assertTrue(seat.book());
        assertEquals(SeatState.BOOKED, seat.getState());
        assertFalse(seat.isAvailable());
    }

    @Test
    void cannotBookSeatTwice() {
        Seat seat = new Seat(1L, 'A', 3);
        seat.book();

        assertFalse(seat.book());
    }

    @Test
    void equalityIsBasedOnPosition() {
        Seat first = new Seat(1L, 'A', 3);
        Seat second = new Seat(99L, 'a', 3);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertFalse(first.equals(new Seat(2L, 'B', 3)));
    }
}