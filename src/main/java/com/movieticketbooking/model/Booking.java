package com.movieticketbooking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {

    private final long id;
    private final Customer customer;
    private final Show show;
    private final List<Seat> seats;
    private final Ticket ticket;
    private final LocalDateTime createdAt;
    private String bookingCode;
    private boolean confirmed;

    public Booking(long id, Customer customer, Show show, List<Seat> seats, Ticket ticket) {
        if (customer == null) {
            throw new IllegalArgumentException("customer must not be null");
        }
        if (show == null) {
            throw new IllegalArgumentException("show must not be null");
        }
        if (seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("at least one seat is required");
        }
        if (ticket == null) {
            throw new IllegalArgumentException("ticket must not be null");
        }
        List<Seat> unique = new ArrayList<>();
        for (Seat seat : seats) {
            if (!show.hasSeat(seat)) {
                throw new IllegalArgumentException("seat does not belong to show: " + seat.getLabel());
            }
            if (unique.contains(seat)) {
                throw new IllegalArgumentException("duplicate seat in booking: " + seat.getLabel());
            }
            unique.add(seat);
        }
        this.id = id;
        this.customer = customer;
        this.show = show;
        this.seats = List.copyOf(unique);
        this.ticket = ticket;
        this.createdAt = LocalDateTime.now();
        this.confirmed = false;
    }

    public long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void assignBookingCode(String bookingCode) {
        if (bookingCode == null || bookingCode.isBlank()) {
            throw new IllegalArgumentException("bookingCode must not be blank");
        }
        this.bookingCode = bookingCode;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void confirm() {
        for (Seat seat : seats) {
            if (!seat.isAvailable()) {
                throw new IllegalStateException("seat is not available: " + seat.getLabel());
            }
        }
        for (Seat seat : seats) {
            seat.book();
        }
        confirmed = true;
    }
}