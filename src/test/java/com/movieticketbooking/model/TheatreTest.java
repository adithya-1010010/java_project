package com.movieticketbooking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TheatreTest {

    @Test
    void createsTheatreWithAttributes() {
        Theatre theatre = new Theatre(1L, "CineMax", "Downtown");

        assertEquals(1L, theatre.getId());
        assertEquals("CineMax", theatre.getName());
        assertEquals("Downtown", theatre.getLocation());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Theatre(1L, "   ", "Downtown"));
    }
}