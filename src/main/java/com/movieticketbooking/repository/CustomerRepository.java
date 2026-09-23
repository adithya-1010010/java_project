package com.movieticketbooking.repository;

import com.movieticketbooking.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerRepository {

    public Optional<Customer> findById(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, full_name, email, phone FROM customers WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return Optional.of(map(rows));
            }
        }
        return Optional.empty();
    }

    public long insert(Connection connection, Customer customer) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO customers (full_name, email, phone) VALUES (?, ?, ?)")) {
            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.executeUpdate();
        }
        return lastInsertId(connection);
    }

    private Customer map(ResultSet rows) throws SQLException {
        return new Customer(
                rows.getString("full_name"),
                rows.getString("email"),
                rows.getString("phone"));
    }

    private long lastInsertId(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT last_insert_rowid()")) {
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                return rows.getLong(1);
            }
            throw new SQLException("no id returned after insert");
        }
    }
}