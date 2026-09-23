package com.movieticketbooking.model;

import com.movieticketbooking.model.pricing.DiscountedPricing;
import com.movieticketbooking.model.pricing.StandardPricing;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketTest {

    @Test
    void computesQuantityFromSelectedSeats() {
        Show show = showWith2x3();
        Ticket ticket = new Ticket(show, show.getSeats().subList(0, 2), show.getMovie().getTicketPrice(),
                new StandardPricing());

        assertEquals(2, ticket.getQuantity());
        assertEquals(show.getMovie().getTicketPrice(), ticket.getUnitPrice());
    }

    @Test
    void computesStandardTotal() {
        Show show = showWith2x3();
        Ticket ticket = new Ticket(show, show.getSeats().subList(0, 2), show.getMovie().getTicketPrice(),
                new StandardPricing());

        assertEquals(new BigDecimal("24.00"), ticket.getTotal());
    }

    @Test
    void appliesDiscountedStrategyDifferently() {
        Show show = showWith2x3();
        List<Seat> seats = show.getSeats();
        Ticket standard = new Ticket(show, seats, show.getMovie().getTicketPrice(), new StandardPricing());
        Ticket discounted = new Ticket(show, seats, show.getMovie().getTicketPrice(),
                new DiscountedPricing(5, new BigDecimal("10")));

        assertEquals(new BigDecimal("72.00"), standard.getTotal());
        assertEquals(new BigDecimal("64.80"), discounted.getTotal());
    }

    @Test
    void rejectsSeatsNotBelongingToShow() {
        Show show = showWith2x3();
        Seat foreign = new Seat(999L, 'Z', 9);

        assertThrows(IllegalArgumentException.class,
                () -> new Ticket(show, List.of(foreign), new BigDecimal("10.00"), new StandardPricing()));
    }

    @Test
    void rejectsDuplicateSeats() {
        Show show = showWith2x3();
        Seat seat = show.getSeats().get(0);

        assertThrows(IllegalArgumentException.class,
                () -> new Ticket(show, List.of(seat, seat), new BigDecimal("10.00"), new StandardPricing()));
    }

    @Test
    void rejectsEmptySeatList() {
        Show show = showWith2x3();
        assertThrows(IllegalArgumentException.class,
                () -> new Ticket(show, List.of(), new BigDecimal("10.00"), new StandardPricing()));
    }

    private Show showWith2x3() {
        List<Seat> seats = TestFixtures.seats(2, 3);
        return TestFixtures.show(TestFixtures.movie(), TestFixtures.theatre(), seats);
    }
}