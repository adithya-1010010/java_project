package com.movieticketbooking.controller;

import com.movieticketbooking.model.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeController {

    public Scene createScene(User user) {
        Label welcome = new Label("Welcome, " + user.getFullName());
        welcome.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label hint = new Label("Movie browsing arrives in the next phase. For now, log in to reach this home screen.");

        VBox root = new VBox(20, welcome, hint);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }
}