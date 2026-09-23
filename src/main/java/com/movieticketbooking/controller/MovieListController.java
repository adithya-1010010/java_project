package com.movieticketbooking.controller;

import com.movieticketbooking.model.Movie;
import com.movieticketbooking.service.MovieService;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public class MovieListController {

    private final MovieService movieService;
    private final Consumer<Movie> onMovieSelected;
    private final Runnable onBack;

    public MovieListController(MovieService movieService, Consumer<Movie> onMovieSelected, Runnable onBack) {
        this.movieService = movieService;
        this.onMovieSelected = onMovieSelected;
        this.onBack = onBack;
    }

    public Scene createScene() {
        Label title = new Label("Available Movies");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        List<Movie> movies = movieService.listMovies();
        ListView<Movie> list = new ListView<>(FXCollections.observableArrayList(movies));
        list.setPrefHeight(420);
        list.setCellFactory(view -> new ListCell<>() {
            @Override
            protected void updateItem(Movie movie, boolean empty) {
                super.updateItem(movie, empty);
                if (empty || movie == null) {
                    setText(null);
                } else {
                    setText(movie.getTitle() + "   |   " + movie.getGenre() + "   |   $"
                            + movie.getTicketPrice() + "   |   " + movie.getDurationMinutes() + " min");
                }
            }
        });

        Button select = new Button("Select Movie");
        select.setOnAction(event -> {
            Movie movie = list.getSelectionModel().getSelectedItem();
            if (movie != null) {
                onMovieSelected.accept(movie);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(event -> onBack.run());

        Label empty = new Label(movies.isEmpty() ? "No movies available." : "");
        if (movies.isEmpty()) {
            select.setDisable(true);
        }

        VBox root = new VBox(12, title, empty, list, select, back);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }
}