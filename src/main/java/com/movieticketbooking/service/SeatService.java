package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.repository.SeatRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SeatService {

    private final Database database;
    private final SeatRepository seats;

    public SeatService(Database database) {
        this.database = database;
        this.seats = new SeatRepository();
    }

    public List<Seat> seatsForShow(long showId) {
        try (Connection connection = database.open()) {
            return seats.findByShow(connection, showId);
        } catch (SQLException e) {
            throw new IllegalStateException("could not load seats", e);
        }
    }
}