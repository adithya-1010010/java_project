package com.movieticketbooking;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Movie Ticket Booking System");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Movie Ticket Booking System");
        stage.setScene(scene);
        stage.show();

        System.out.println("APPLICATION_STARTED");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
