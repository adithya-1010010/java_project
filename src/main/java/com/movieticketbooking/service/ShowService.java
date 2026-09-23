package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.repository.MovieRepository;
import com.movieticketbooking.repository.ShowRepository;
import com.movieticketbooking.repository.TheatreRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ShowService {

    private final Database database;
    private final ShowRepository shows;

    public ShowService(Database database) {
        this.database = database;
        this.shows = new ShowRepository(new MovieRepository(), new TheatreRepository());
    }

    public List<Show> showsForMovie(long movieId) {
        try (Connection connection = database.open()) {
            return shows.findByMovieId(connection, movieId);
        } catch (SQLException e) {
            throw new IllegalStateException("could not load shows", e);
        }
    }
}