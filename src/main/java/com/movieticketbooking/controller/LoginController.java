package com.movieticketbooking.controller;

import com.movieticketbooking.model.User;
import com.movieticketbooking.service.AuthenticationService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class LoginController {

    private final AuthenticationService authenticationService;
    private final Consumer<User> onAuthenticated;
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label errorLabel = new Label();

    public LoginController(AuthenticationService authenticationService, Consumer<User> onAuthenticated) {
        this.authenticationService = authenticationService;
        this.onAuthenticated = onAuthenticated;
    }

    public Scene createScene() {
        Label title = new Label("Movie Ticket Booking System");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginButton = new Button("Login");
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(event -> login());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(errorLabel, 1, 3);

        VBox root = new VBox(15, title, grid);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }

    private void login() {
        boolean success = new LoginGate(authenticationService)
                .attempt(usernameField.getText(), passwordField.getText(), onAuthenticated);
        if (!success) {
            errorLabel.setText("Invalid username or password.");
        }
    }
}