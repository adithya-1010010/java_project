package com.movieticketbooking.config;

import com.movieticketbooking.model.Movie;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.User;
import com.movieticketbooking.repository.MovieRepository;
import com.movieticketbooking.repository.ShowRepository;
import com.movieticketbooking.repository.TheatreRepository;
import com.movieticketbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseInitTest {

    @TempDir
    Path dir;

    private static final Set<String> EXPECTED_TABLES = Set.of(
            "users", "movies", "theatres", "shows", "seats", "customers", "bookings", "booking_seats");

    @Test
    void createsAllTablesFromCleanState() {
        Database database = newDatabase();

        database.init();

        try (Connection connection = database.open()) {
            Set<String> tables = new HashSet<>();
            ResultSet rows = connection.createStatement().executeQuery(
                    "SELECT name FROM sqlite_master WHERE type = 'table'");
            while (rows.next()) {
                tables.add(rows.getString("name"));
            }
            assertTrue(tables.containsAll(EXPECTED_TABLES));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void loadsSeedCatalog() {
        Database database = newDatabase();
        database.init();
        MovieRepository movies = new MovieRepository();
        ShowRepository shows = new ShowRepository(new MovieRepository(), new TheatreRepository());
        UserRepository users = new UserRepository();
        SeatProbe seats = new SeatProbe();

        try (Connection connection = database.open()) {
            List<Movie> allMovies = movies.findAll(connection);
            assertEquals(4, allMovies.size());
            List<Show> allShows = shows.findAll(connection);
            assertTrue(allShows.size() >= 4);
            for (Show show : allShows) {
                assertEquals(80, seats.countForShow(connection, show.getId()));
            }
            assertTrue(users.findByUsername(connection, "demo").isPresent());
            assertTrue(users.findByUsername(connection, "alice").isPresent());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void initIsIdempotent() {
        Database database = newDatabase();

        database.init();
        database.init();
        database.init();

        try (Connection connection = database.open()) {
            assertEquals(4, new MovieRepository().findAll(connection).size());
            assertEquals(80, new SeatProbe().countForShow(connection, 1L));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void seatLayoutIsUniquePerShow() {
        Database database = newDatabase();
        database.init();

        try (Connection connection = database.open();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT OR IGNORE INTO seats (show_id, row_label, column_number) VALUES (?, ?, ?)")) {
            statement.setLong(1, 1L);
            statement.setString(2, "A");
            statement.setInt(3, 1);
            assertEquals(0, statement.executeUpdate(), "duplicate (show, row, column) must be ignored");
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void foreignKeysAreEnforced() {
        Database database = newDatabase();
        database.init();

        try (Connection connection = database.open();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO shows (id, movie_id, theatre_id, start_time, end_time) VALUES (?, ?, ?, ?, ?)")) {
            statement.setLong(1, 99L);
            statement.setLong(2, 4242L);
            statement.setLong(3, 1L);
            statement.setString(4, "2026-11-01T10:00");
            statement.setString(5, "2026-11-01T12:00");
            assertThrows(SQLException.class, statement::executeUpdate);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void seedUsersHaveHashedPasswords() {
        Database database = newDatabase();
        database.init();

        try (Connection connection = database.open()) {
            User demo = new UserRepository().findByUsername(connection, "demo").orElseThrow();
            assertTrue(demo.getPasswordHash().length() == 64, "expected a 64-char SHA-256 hex hash");
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Database newDatabase() {
        return new Database(dir.resolve("test.db").toString());
    }

    private static final class SeatProbe {
        int countForShow(Connection connection, long showId) throws SQLException {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM seats WHERE show_id = ?")) {
                statement.setLong(1, showId);
                ResultSet rows = statement.executeQuery();
                return rows.next() ? rows.getInt(1) : 0;
            }
        }
    }
}