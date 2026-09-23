package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShowServiceTest {

    @TempDir
    Path dir;

    private ShowService showService;

    @BeforeEach
    void setUp() {
        Database database = new Database(dir.resolve("shows.db").toString());
        database.init();
        showService = new ShowService(database);
    }

    @Test
    void showsAreFilteredByMovie() {
        List<Show> inceptionShows = showService.showsForMovie(1L);
        List<Show> godfatherShows = showService.showsForMovie(4L);

        assertEquals(2, inceptionShows.size());
        assertTrue(godfatherShows.isEmpty());
    }

    @Test
    void showsCarryTheatreAndTiming() {
        Show show = showService.showsForMovie(1L).get(0);

        assertEquals("CineMax", show.getTheatre().getName());
        assertEquals(1L, show.getMovie().getId());
        assertTrue(show.getStartTime().isBefore(show.getEndTime()));
    }

    @Test
    void unknownMovieHasNoShows() {
        assertTrue(showService.showsForMovie(999L).isEmpty());
    }
}