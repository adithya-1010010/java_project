package com.movieticketbooking;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.controller.HomeController;
import com.movieticketbooking.controller.LoginController;
import com.movieticketbooking.model.User;
import com.movieticketbooking.service.AuthenticationService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Database database = new Database(defaultDatabasePath());
        database.init();

        AuthenticationService authenticationService = new AuthenticationService(database);
        LoginController login = new LoginController(authenticationService, user -> home(stage, user));

        stage.setTitle("Movie Ticket Booking System");
        stage.setScene(login.createScene());
        stage.show();

        System.out.println("APPLICATION_STARTED");
    }

    private void home(Stage stage, User user) {
        stage.setScene(new HomeController().createScene(user));
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