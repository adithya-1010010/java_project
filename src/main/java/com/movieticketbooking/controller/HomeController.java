package com.movieticketbooking.controller;

import com.movieticketbooking.model.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeController {

    private final Runnable onBrowseMovies;

    public HomeController(Runnable onBrowseMovies) {
        this.onBrowseMovies = onBrowseMovies;
    }

    public Scene createScene(User user) {
        Label welcome = new Label("Welcome, " + user.getFullName());
        welcome.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button browse = new Button("Browse Movies");
        browse.setOnAction(event -> onBrowseMovies.run());

        VBox root = new VBox(20, welcome, browse);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }
}