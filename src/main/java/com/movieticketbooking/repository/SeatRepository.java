package com.movieticketbooking.repository;

import com.movieticketbooking.model.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeatRepository {

    public List<Seat> findByShow(Connection connection, long showId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, row_label, column_number, state FROM seats WHERE show_id = ? ORDER BY row_label, column_number")) {
            statement.setLong(1, showId);
            ResultSet rows = statement.executeQuery();
            while (rows.next()) {
                Seat seat = new Seat(rows.getLong("id"), rows.getString("row_label").charAt(0), rows.getInt("column_number"));
                if ("BOOKED".equals(rows.getString("state"))) {
                    seat.book();
                }
                seats.add(seat);
            }
        }
        return seats;
    }

    public Optional<Seat> findById(Connection connection, long id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT id, show_id, row_label, column_number, state FROM seats WHERE id = ?")) {
            statement.setLong(1, id);
            ResultSet rows = statement.executeQuery();
            if (rows.next()) {
                Seat seat = new Seat(rows.getLong("id"), rows.getString("row_label").charAt(0), rows.getInt("column_number"));
                if ("BOOKED".equals(rows.getString("state"))) {
                    seat.book();
                }
                return Optional.of(seat);
            }
        }
        return Optional.empty();
    }

    public boolean hasSeats(Connection connection, long showId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM seats WHERE show_id = ?")) {
            statement.setLong(1, showId);
            ResultSet rows = statement.executeQuery();
            return rows.next() && rows.getInt(1) > 0;
        }
    }

    public void insertSeats(Connection connection, long showId, List<Seat> seats) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT OR IGNORE INTO seats (show_id, row_label, column_number) VALUES (?, ?, ?)")) {
            for (Seat seat : seats) {
                statement.setLong(1, showId);
                statement.setString(2, Character.toString(seat.getRow()));
                statement.setInt(3, seat.getColumn());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    public void markBooked(Connection connection, List<Seat> seats) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE seats SET state = 'BOOKED' WHERE id = ?")) {
            for (Seat seat : seats) {
                statement.setLong(1, seat.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}