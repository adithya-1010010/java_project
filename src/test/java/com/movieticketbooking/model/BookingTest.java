package com.movieticketbooking.model;

import com.movieticketbooking.model.pricing.StandardPricing;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingTest {

    @Test
    void confirmMarksSeatsBooked() {
        Booking booking = newBooking(2);

        assertFalse(booking.isConfirmed());
        booking.confirm();

        assertTrue(booking.isConfirmed());
        for (Seat seat : booking.getSeats()) {
            assertEquals(SeatState.BOOKED, seat.getState());
        }
    }

    @Test
    void cannotConfirmBookedSeats() {
        Show show = TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), TestFixtures.seats(1, 3));
        Seat seat = show.getSeats().get(1);
        seat.book();

        Booking booking = buildBooking(show, TestFixtures.customer(), List.of(seat));

        assertThrows(IllegalStateException.class, booking::confirm);
        assertFalse(booking.isConfirmed());
    }

    @Test
    void assignsBookingCode() {
        Booking booking = newBooking(1);
        booking.confirm();

        booking.assignBookingCode("BK-2026-0001");
        assertEquals("BK-2026-0001", booking.getBookingCode());
        assertThrows(IllegalArgumentException.class, () -> booking.assignBookingCode(" "));
    }

    @Test
    void rejectsEmptySeats() {
        Show show = TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), TestFixtures.seats(1, 3));
        Ticket ticket = new Ticket(show, show.getSeats().subList(0, 1), show.getMovie().getTicketPrice(),
                new StandardPricing());

        assertThrows(IllegalArgumentException.class,
                () -> new Booking(1L, TestFixtures.customer(), show, List.of(), ticket));
    }

    @Test
    void rejectsSeatNotBelongingToShow() {
        Show show = TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), TestFixtures.seats(1, 3));
        Seat foreign = new Seat(999L, 'Z', 9);
        Ticket ticket = new Ticket(show, show.getSeats().subList(0, 1), show.getMovie().getTicketPrice(),
                new StandardPricing());

        assertThrows(IllegalArgumentException.class,
                () -> new Booking(1L, TestFixtures.customer(), show, List.of(foreign), ticket));
    }

    @Test
    void rejectsDuplicateSeats() {
        Show show = TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), TestFixtures.seats(1, 3));
        Seat seat = show.getSeats().get(0);
        Ticket ticket = new Ticket(show, List.of(seat), show.getMovie().getTicketPrice(), new StandardPricing());

        assertThrows(IllegalArgumentException.class,
                () -> new Booking(1L, TestFixtures.customer(), show, List.of(seat, seat), ticket));
    }

    @Test
    void totalIsComputedThroughTicket() {
        Booking booking = newBooking(2);

        assertEquals(new BigDecimal("24.00"), booking.getTicket().getTotal());
    }

    private Booking newBooking(int seatCount) {
        Show show = TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), TestFixtures.seats(1, 3));
        List<Seat> seats = show.getSeats().subList(0, seatCount);
        return buildBooking(show, TestFixtures.customer(), seats);
    }

    private Booking buildBooking(Show show, Customer customer, List<Seat> seats) {
        Ticket ticket = new Ticket(show, seats, show.getMovie().getTicketPrice(), new StandardPricing());
        return new Booking(1L, customer, show, seats, ticket);
    }
}