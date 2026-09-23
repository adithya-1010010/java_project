package com.movieticketbooking.repository;

import com.movieticketbooking.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {

    public Optional<User> findByUsername(Connection connection, String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT username, password_hash, full_name, email, phone FROM users WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return Optional.of(map(rows));
            }
        }
        return Optional.empty();
    }

    public void insert(Connection connection, User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO users (username, password_hash, full_name, email, phone) VALUES (?, ?, ?, ?, ?)")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPhone());
            statement.executeUpdate();
        }
    }

    private User map(ResultSet rows) throws SQLException {
        return new User(
                rows.getString("full_name"),
                rows.getString("email"),
                rows.getString("phone"),
                rows.getString("username"),
                rows.getString("password_hash"));
    }
}