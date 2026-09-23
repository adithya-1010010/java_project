package com.movieticketbooking.repository;

import com.movieticketbooking.model.Theatre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TheatreRepository {

    public List<Theatre> findAll(Connection connection) throws SQLException {
        List<Theatre> theatres = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name, location FROM theatres ORDER BY id")) {
            ResultSet rows = statement.executeQuery();
            while (rows.next()) {
                theatres.add(map(rows));
            }
        }
        return theatres;
    }

    public Optional<Theatre> findById(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, name, location FROM theatres WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return Optional.of(map(rows));
            }
        }
        return Optional.empty();
    }

    private Theatre map(ResultSet rows) throws SQLException {
        return new Theatre(
                rows.getLong("id"),
                rows.getString("name"),
                rows.getString("location"));
    }
}