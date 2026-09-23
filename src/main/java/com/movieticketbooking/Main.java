package com.movieticketbooking;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.controller.HomeController;
import com.movieticketbooking.controller.LoginController;
import com.movieticketbooking.controller.MovieListController;
import com.movieticketbooking.controller.PlaceholderController;
import com.movieticketbooking.controller.ShowController;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.User;
import com.movieticketbooking.service.AuthenticationService;
import com.movieticketbooking.service.MovieService;
import com.movieticketbooking.service.ShowService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    private Stage stage;
    private User currentUser;
    private Database database;
    private MovieService movieService;
    private ShowService showService;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.database = new Database(defaultDatabasePath());
        this.database.init();
        this.movieService = new MovieService(database);
        this.showService = new ShowService(database);

        AuthenticationService authenticationService = new AuthenticationService(database);
        LoginController login = new LoginController(authenticationService, this::authenticated);

        stage.setTitle("Movie Ticket Booking System");
        stage.setScene(login.createScene());
        stage.show();

        System.out.println("APPLICATION_STARTED");
    }

    private void authenticated(User user) {
        this.currentUser = user;
        showHome();
    }

    private void showHome() {
        HomeController home = new HomeController(this::showMovies);
        stage.setScene(home.createScene(currentUser));
    }

    private void showMovies() {
        MovieListController movies = new MovieListController(
                movieService,
                this::showShows,
                this::showHome);
        stage.setScene(movies.createScene());
    }

    private void showShows(com.movieticketbooking.model.Movie movie) {
        ShowController shows = new ShowController(
                showService,
                this::showSeatPlaceholder,
                this::showMovies);
        stage.setScene(shows.createScene(movie.getId(), movie.getTitle()));
    }

    private void showSeatPlaceholder(Show show) {
        stage.setScene(new PlaceholderController(this::showShowsBack)
                .createScene("Seat selection is coming in the next phase."));
    }

    private void showShowsBack() {
        showMovies();
    }

    private String defaultDatabasePath() {
        try {
            Path directory = Paths.get(System.getProperty("user.home"), ".movieticket");
            Files.createDirectories(directory);
            return directory.resolve("movieticket.db").toString();
        } catch (IOException e) {
            throw new IllegalStateException("could not prepare the local database directory", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}