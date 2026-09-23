package com.movieticketbooking.config;

import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.User;
import com.movieticketbooking.repository.MovieRepository;
import com.movieticketbooking.repository.SeatRepository;
import com.movieticketbooking.repository.ShowRepository;
import com.movieticketbooking.repository.TheatreRepository;
import com.movieticketbooking.repository.UserRepository;
import com.movieticketbooking.util.PasswordHasher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class Seeder {

    private final Database database;

    public Seeder(Database database) {
        this.database = database;
    }

    public void seed(Connection connection) throws SQLException {
        seedSeats(connection);
        seedUsers(connection);
    }

    private void seedSeats(Connection connection) throws SQLException {
        ShowRepository shows = new ShowRepository(new MovieRepository(), new TheatreRepository());
        SeatRepository seats = new SeatRepository();
        for (Show show : shows.findAll(connection)) {
            if (!seats.hasSeats(connection, show.getId())) {
                seats.insertSeats(connection, show.getId(), buildLayout());
            }
        }
    }

    private void seedUsers(Connection connection) throws SQLException {
        UserRepository users = new UserRepository();
        if (users.findByUsername(connection, "demo").isEmpty()) {
            users.insert(connection, new User("Demo User", "demo@example.com", "555-0001", "demo",
                    PasswordHasher.sha256("demo123")));
        }
        if (users.findByUsername(connection, "alice").isEmpty()) {
            users.insert(connection, new User("Alice Johnson", "alice@example.com", "555-0002", "alice",
                    PasswordHasher.sha256("alice123")));
        }
    }

    private List<Seat> buildLayout() {
        List<Seat> layout = new ArrayList<>();
        long id = 1;
        for (char row = 'A'; row <= 'H'; row++) {
            for (int column = 1; column <= 10; column++) {
                layout.add(new Seat(id++, row, column));
            }
        }
        return layout;
    }
}