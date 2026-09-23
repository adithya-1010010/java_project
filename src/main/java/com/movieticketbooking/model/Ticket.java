package com.movieticketbooking.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private final Show show;
    private final List<Seat> seats;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal total;

    public Ticket(Show show, List<Seat> seats, BigDecimal baseUnitPrice,
                  com.movieticketbooking.model.pricing.PricingStrategy strategy) {
        this(show, seats, requireUnitPrice(baseUnitPrice), requireStrategy(strategy).price(baseUnitPrice, seats.size()));
    }

    private Ticket(Show show, List<Seat> seats, BigDecimal unitPrice, BigDecimal total) {
        if (show == null) {
            throw new IllegalArgumentException("show must not be null");
        }
        if (seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("at least one seat is required");
        }
        List<Seat> unique = new ArrayList<>();
        for (Seat seat : seats) {
            if (!show.hasSeat(seat)) {
                throw new IllegalArgumentException("seat does not belong to show: " + seat.getLabel());
            }
            if (unique.contains(seat)) {
                throw new IllegalArgumentException("duplicate seat in ticket: " + seat.getLabel());
            }
            unique.add(seat);
        }
        this.show = show;
        this.seats = List.copyOf(unique);
        this.quantity = unique.size();
        this.unitPrice = unitPrice;
        this.total = total;
    }

    public static Ticket reconstruct(Show show, List<Seat> seats, BigDecimal unitPrice, BigDecimal total) {
        if (total == null || total.signum() < 0) {
            throw new IllegalArgumentException("total must be a non-negative value");
        }
        return new Ticket(show, seats, requireUnitPrice(unitPrice), total);
    }

    private static BigDecimal requireUnitPrice(BigDecimal baseUnitPrice) {
        if (baseUnitPrice == null || baseUnitPrice.signum() < 0) {
            throw new IllegalArgumentException("baseUnitPrice must be a non-negative value");
        }
        return baseUnitPrice;
    }

    private static com.movieticketbooking.model.pricing.PricingStrategy requireStrategy(
            com.movieticketbooking.model.pricing.PricingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("pricing strategy must not be null");
        }
        return strategy;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }
}