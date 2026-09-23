package com.movieticketbooking.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeatSelectionTest {

    @Test
    void selectsAnAvailableSeat() {
        SeatSelection selection = new SeatSelection();
        Seat seat = new Seat(1L, 'A', 1);

        assertTrue(selection.toggle(seat));
        assertEquals(1, selection.size());
        assertTrue(selection.isSelected(seat));
    }

    @Test
    void togglingAgainDeselects() {
        SeatSelection selection = new SeatSelection();
        Seat seat = new Seat(1L, 'A', 1);

        selection.toggle(seat);
        selection.toggle(seat);

        assertEquals(0, selection.size());
        assertFalse(selection.isSelected(seat));
    }

    @Test
    void bookedSeatCannotBeSelected() {
        SeatSelection selection = new SeatSelection();
        Seat seat = new Seat(1L, 'A', 1);
        seat.book();

        assertFalse(selection.toggle(seat));
        assertEquals(0, selection.size());
    }

    @Test
    void nullSeatIsRejected() {
        SeatSelection selection = new SeatSelection();

        assertFalse(selection.toggle(null));
        assertEquals(0, selection.size());
    }

    @Test
    void multipleSeatsCanBeSelected() {
        SeatSelection selection = new SeatSelection();
        Seat first = new Seat(1L, 'A', 1);
        Seat second = new Seat(2L, 'A', 2);

        selection.toggle(first);
        selection.toggle(second);

        assertEquals(List.of(first, second), selection.getSelected());
    }

    @Test
    void clearRemovesAllSelections() {
        SeatSelection selection = new SeatSelection();
        selection.toggle(new Seat(1L, 'A', 1));
        selection.toggle(new Seat(2L, 'A', 2));

        selection.clear();

        assertEquals(0, selection.size());
    }
}