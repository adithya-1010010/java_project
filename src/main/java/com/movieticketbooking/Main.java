package com.movieticketbooking;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.controller.BookingSummaryController;
import com.movieticketbooking.controller.ConfirmationController;
import com.movieticketbooking.controller.CustomerController;
import com.movieticketbooking.controller.HomeController;
import com.movieticketbooking.controller.LoginController;
import com.movieticketbooking.controller.MovieListController;
import com.movieticketbooking.controller.PlaceholderController;
import com.movieticketbooking.controller.SeatController;
import com.movieticketbooking.controller.ShowController;
import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Customer;
import com.movieticketbooking.model.Movie;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.User;
import com.movieticketbooking.service.AuthenticationService;
import com.movieticketbooking.service.BookingService;
import com.movieticketbooking.service.MovieService;
import com.movieticketbooking.service.SeatService;
import com.movieticketbooking.service.ShowService;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {

    private Stage stage;
    private User currentUser;
    private Database database;
    private MovieService movieService;
    private ShowService showService;
    private SeatService seatService;
    private BookingService bookingService;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.database = new Database(defaultDatabasePath());
        this.database.init();
        this.movieService = new MovieService(database);
        this.showService = new ShowService(database);
        this.seatService = new SeatService(database);
        this.bookingService = new BookingService(database);

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

    private void showShows(Movie movie) {
        ShowController shows = new ShowController(showService, this::showSeats, this::showMovies);
        stage.setScene(shows.createScene(movie.getId(), movie.getTitle()));
    }

    private void showSeats(Show show) {
        SeatController seats = new SeatController(
                seatService,
                selected -> showCustomerDetails(show, selected),
                this::showMovies);
        stage.setScene(seats.createScene(show));
    }

    private void showCustomerDetails(Show show, List<Seat> seats) {
        CustomerController form = new CustomerController(
                (customer, selected) -> showReview(show, selected, customer),
                () -> showSeats(show));
        stage.setScene(form.createScene(show, seats));
    }

    private void showReview(Show show, List<Seat> seats, Customer customer) {
        BookingSummaryController summary = new BookingSummaryController(
                () -> confirmBooking(show, seats, customer),
                () -> showCustomerDetails(show, seats));
        stage.setScene(summary.createScene(show, customer, seats));
    }

    private void confirmBooking(Show show, List<Seat> seats, Customer customer) {
        try {
            Booking booking = bookingService.confirmBooking(customer, show, seats);
            ConfirmationController confirmation = new ConfirmationController(this::showMovies, this::showHome);
            stage.setScene(confirmation.createScene(booking));
        } catch (IllegalStateException e) {
            stage.setScene(new PlaceholderController(() -> showSeats(show))
                    .createScene(e.getMessage() + "\nPlease select different seats."));
        }
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