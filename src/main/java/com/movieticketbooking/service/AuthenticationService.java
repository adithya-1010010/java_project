package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.User;
import com.movieticketbooking.repository.UserRepository;
import com.movieticketbooking.util.PasswordHasher;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class AuthenticationService {

    private final Database database;
    private final UserRepository users;

    public AuthenticationService(Database database) {
        this.database = database;
        this.users = new UserRepository();
    }

    public Optional<User> authenticate(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            return Optional.empty();
        }
        String candidateHash = PasswordHasher.sha256(password);
        try (Connection connection = database.open()) {
            return users.findByUsername(connection, username.trim())
                    .filter(user -> constantTimeEquals(user.getPasswordHash(), candidateHash));
        } catch (SQLException e) {
            throw new IllegalStateException("authentication could not be performed", e);
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static boolean constantTimeEquals(String left, String right) {
        return MessageDigest.isEqual(
                left.getBytes(StandardCharsets.UTF_8),
                right.getBytes(StandardCharsets.UTF_8));
    }
}