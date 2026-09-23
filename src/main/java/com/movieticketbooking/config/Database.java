package com.movieticketbooking.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final String path;

    public Database(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("path must not be blank");
        }
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return "jdbc:sqlite:" + path;
    }

    public Connection open() throws SQLException {
        Connection connection = DriverManager.getConnection(getUrl());
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public void init() {
        try (Connection connection = open()) {
            runScript(connection, "/schema.sql");
            runScript(connection, "/seed.sql");
            new Seeder(this).seed(connection);
        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Failed to initialize database at " + path, e);
        }
    }

    private void runScript(Connection connection, String resource) throws SQLException, IOException {
        try (InputStream input = Database.class.getResourceAsStream(resource)) {
            if (input == null) {
                throw new IllegalStateException("missing resource " + resource);
            }
            String script = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            for (String statement : script.split(";")) {
                String trimmed = statement.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                try (Statement sql = connection.createStatement()) {
                    sql.execute(trimmed);
                }
            }
        }
    }
}