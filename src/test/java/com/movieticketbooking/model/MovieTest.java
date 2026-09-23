package com.movieticketbooking.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovieTest {

    @Test
    void createsMovieWithAttributes() {
        Movie movie = new Movie(1L, "Inception", MovieGenre.SCI_FI, new BigDecimal("12.00"), 148, "Synopsis");

        assertEquals(1L, movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals(MovieGenre.SCI_FI, movie.getGenre());
        assertEquals(new BigDecimal("12.00"), movie.getTicketPrice());
        assertEquals(148, movie.getDurationMinutes());
        assertEquals("Synopsis", movie.getSynopsis());
    }

    @Test
    void rejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1L, "  ", MovieGenre.DRAMA, new BigDecimal("12.00"), 120, null));
    }

    @Test
    void rejectsNullGenre() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1L, "Inception", null, new BigDecimal("12.00"), 148, null));
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1L, "Inception", MovieGenre.DRAMA, new BigDecimal("-1"), 148, null));
    }

    @Test
    void allowsFreeMovie() {
        Movie movie = new Movie(1L, "Promo", MovieGenre.DRAMA, BigDecimal.ZERO, 120, null);
        assertEquals(BigDecimal.ZERO, movie.getTicketPrice());
    }

    @Test
    void rejectsNonPositiveDuration() {
        assertThrows(IllegalArgumentException.class,
                () -> new Movie(1L, "Inception", MovieGenre.DRAMA, new BigDecimal("12.00"), 0, null));
    }
}