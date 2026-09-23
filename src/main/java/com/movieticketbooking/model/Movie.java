package com.movieticketbooking.model;

import java.math.BigDecimal;

public class Movie {

    private final long id;
    private final String title;
    private final MovieGenre genre;
    private final BigDecimal ticketPrice;
    private final int durationMinutes;
    private final String synopsis;

    public Movie(long id, String title, MovieGenre genre, BigDecimal ticketPrice, int durationMinutes, String synopsis) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (genre == null) {
            throw new IllegalArgumentException("genre must not be null");
        }
        if (ticketPrice == null || ticketPrice.signum() < 0) {
            throw new IllegalArgumentException("ticketPrice must be a non-negative value");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("durationMinutes must be positive");
        }
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.ticketPrice = ticketPrice;
        this.durationMinutes = durationMinutes;
        this.synopsis = synopsis;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public MovieGenre getGenre() {
        return genre;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getSynopsis() {
        return synopsis;
    }
}