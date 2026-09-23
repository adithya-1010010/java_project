package com.movieticketbooking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Show {

    private final long id;
    private final Movie movie;
    private final Theatre theatre;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<Seat> seats;

    public Show(long id, Movie movie, Theatre theatre, LocalDateTime startTime, LocalDateTime endTime, List<Seat> seats) {
        if (movie == null) {
            throw new IllegalArgumentException("movie must not be null");
        }
        if (theatre == null) {
            throw new IllegalArgumentException("theatre must not be null");
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("startTime and endTime must not be null");
        }
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
        if (seats == null) {
            throw new IllegalArgumentException("seats must not be null");
        }
        List<Seat> unique = new ArrayList<>();
        for (Seat seat : seats) {
            if (unique.contains(seat)) {
                throw new IllegalArgumentException("duplicate seat in show layout: " + seat.getLabel());
            }
            unique.add(seat);
        }
        this.id = id;
        this.movie = movie;
        this.theatre = theatre;
        this.startTime = startTime;
        this.endTime = endTime;
        this.seats = List.copyOf(unique);
    }

    public long getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public List<Seat> getAvailableSeats() {
        return seats.stream().filter(Seat::isAvailable).toList();
    }

    public boolean hasSeat(Seat seat) {
        return seats.stream().anyMatch(s -> s.equals(seat));
    }
}