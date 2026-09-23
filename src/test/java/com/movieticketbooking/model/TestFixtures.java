package com.movieticketbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class TestFixtures {

    private TestFixtures() {
    }

    public static Movie movie() {
        return new Movie(1L, "Inception", MovieGenre.SCI_FI, new BigDecimal("12.00"), 148, "A dream heist");
    }

    public static Theatre theatre() {
        return new Theatre(1L, "CineMax", "Downtown");
    }

    public static List<Seat> seats(int rows, int columns) {
        List<Seat> seats = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            char row = (char) ('A' + r);
            for (int c = 1; c <= columns; c++) {
                seats.add(new Seat(seats.size() + 1L, row, c));
            }
        }
        return seats;
    }

    public static Show show(Movie movie, Theatre theatre, List<Seat> seats) {
        return new Show(
                1L,
                movie,
                theatre,
                LocalDateTime.of(2026, 10, 1, 18, 0),
                LocalDateTime.of(2026, 10, 1, 20, 30),
                seats);
    }

    public static Customer customer() {
        return new Customer("Alice Johnson", "alice@example.com", "555-0100");
    }
}