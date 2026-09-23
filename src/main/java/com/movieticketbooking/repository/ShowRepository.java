package com.movieticketbooking.repository;

import com.movieticketbooking.model.Show;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowRepository {

    private final MovieRepository movies;
    private final TheatreRepository theatres;

    public ShowRepository(MovieRepository movies, TheatreRepository theatres) {
        this.movies = movies;
        this.theatres = theatres;
    }

    public List<Show> findAll(Connection connection) throws SQLException {
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, movie_id, theatre_id, start_time, end_time FROM shows ORDER BY start_time")) {
            ResultSet rows = statement.executeQuery();
            while (rows.next()) {
                shows.add(map(connection, rows));
            }
        }
        return shows;
    }

    public List<Show> findByMovieId(Connection connection, long movieId) throws SQLException {
        List<Show> shows = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, movie_id, theatre_id, start_time, end_time FROM shows WHERE movie_id = ? ORDER BY start_time")) {
            statement.setLong(1, movieId);
            ResultSet rows = statement.executeQuery();
            while (rows.next()) {
                shows.add(map(connection, rows));
            }
        }
        return shows;
    }

    public Optional<Show> findById(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, movie_id, theatre_id, start_time, end_time FROM shows WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return Optional.of(map(connection, rows));
            }
        }
        return Optional.empty();
    }

    private Show map(Connection connection, ResultSet rows) throws SQLException {
        return new Show(
                rows.getLong("id"),
                movies.findById(connection, rows.getLong("movie_id")).orElseThrow(),
                theatres.findById(connection, rows.getLong("theatre_id")).orElseThrow(),
                LocalDateTime.parse(rows.getString("start_time")),
                LocalDateTime.parse(rows.getString("end_time")),
                List.of());
    }
}