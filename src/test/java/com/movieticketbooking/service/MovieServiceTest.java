package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieServiceTest {

    @TempDir
    Path dir;

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        Database database = new Database(dir.resolve("movies.db").toString());
        database.init();
        movieService = new MovieService(database);
    }

    @Test
    void loadsSeededMovies() {
        List<Movie> movies = movieService.listMovies();

        assertEquals(4, movies.size());
    }

    @Test
    void moviesCarryGenreAndPrice() {
        Movie movie = movieService.listMovies().get(0);

        assertTrue(movie.getTitle().equals("Inception"));
        assertEquals("SCI_FI", movie.getGenre().name());
        assertEquals("12.00", movie.getTicketPrice().toPlainString());
    }

    @Test
    void findsMovieById() {
        assertTrue(movieService.findById(1L).isPresent());
        assertTrue(movieService.findById(999L).isEmpty());
    }
}