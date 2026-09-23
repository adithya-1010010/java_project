package com.movieticketbooking.controller;

import com.movieticketbooking.model.Show;
import com.movieticketbooking.service.ShowService;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

public class ShowController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("EEE d MMM yyyy, HH:mm");

    private final ShowService showService;
    private final Consumer<Show> onShowSelected;
    private final Runnable onBack;

    public ShowController(ShowService showService, Consumer<Show> onShowSelected, Runnable onBack) {
        this.showService = showService;
        this.onShowSelected = onShowSelected;
        this.onBack = onBack;
    }

    public Scene createScene(long movieId, String movieTitle) {
        Label title = new Label("Show Timings — " + movieTitle);
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        List<Show> shows = showService.showsForMovie(movieId);
        ListView<Show> list = new ListView<>(FXCollections.observableArrayList(shows));
        list.setPrefHeight(420);
        list.setCellFactory(view -> new ListCell<>() {
            @Override
            protected void updateItem(Show show, boolean empty) {
                super.updateItem(show, empty);
                if (empty || show == null) {
                    setText(null);
                } else {
                    setText(show.getTheatre().getName() + " — " + show.getTheatre().getLocation()
                            + "   |   " + TIME_FORMAT.format(show.getStartTime()));
                }
            }
        });

        Button select = new Button("Select Show");
        select.setOnAction(event -> {
            Show show = list.getSelectionModel().getSelectedItem();
            if (show != null) {
                onShowSelected.accept(show);
            }
        });

        Button back = new Button("Back to Movies");
        back.setOnAction(event -> onBack.run());

        Label empty = new Label(shows.isEmpty() ? "No shows available for this movie." : "");
        if (shows.isEmpty()) {
            select.setDisable(true);
        }

        VBox root = new VBox(12, title, empty, list, select, back);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }
}