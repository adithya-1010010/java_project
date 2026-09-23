package com.movieticketbooking.repository;

import com.movieticketbooking.model.Movie;
import com.movieticketbooking.model.MovieGenre;
import com.movieticketbooking.util.Money;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieRepository {

    public List<Movie> findAll(Connection connection) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, title, genre, ticket_price_cents, duration_minutes, synopsis FROM movies ORDER BY id")) {
            ResultSet rows = statement.executeQuery();
            while (rows.next()) {
                movies.add(map(rows));
            }
        }
        return movies;
    }

    public Optional<Movie> findById(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, title, genre, ticket_price_cents, duration_minutes, synopsis FROM movies WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return Optional.of(map(rows));
            }
        }
        return Optional.empty();
    }

    private Movie map(ResultSet rows) throws SQLException {
        BigDecimal price = Money.fromCents(rows.getLong("ticket_price_cents"));
        return new Movie(
                rows.getLong("id"),
                rows.getString("title"),
                MovieGenre.valueOf(rows.getString("genre")),
                price,
                rows.getInt("duration_minutes"),
                rows.getString("synopsis"));
    }
}