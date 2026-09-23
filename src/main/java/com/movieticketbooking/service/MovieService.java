package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Movie;
import com.movieticketbooking.repository.MovieRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MovieService {

    private final Database database;
    private final MovieRepository movies;

    public MovieService(Database database) {
        this.database = database;
        this.movies = new MovieRepository();
    }

    public List<Movie> listMovies() {
        try (Connection connection = database.open()) {
            return movies.findAll(connection);
        } catch (SQLException e) {
            throw new IllegalStateException("could not load movies", e);
        }
    }

    public Optional<Movie> findById(long id) {
        try (Connection connection = database.open()) {
            return movies.findById(connection, id);
        } catch (SQLException e) {
            throw new IllegalStateException("could not load movie", e);
        }
    }
}