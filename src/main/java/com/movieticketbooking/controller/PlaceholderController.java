package com.movieticketbooking.controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlaceholderController {

    private final Runnable onBack;

    public PlaceholderController(Runnable onBack) {
        this.onBack = onBack;
    }

    public Scene createScene(String message) {
        Label label = new Label(message);
        Button back = new Button("Back");
        back.setOnAction(event -> onBack.run());

        VBox root = new VBox(20, label, back);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }
}