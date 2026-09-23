package com.movieticketbooking.controller;

import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.SeatSelection;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.service.SeatService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SeatController {

    private static final String STYLE_AVAILABLE = "-fx-background-color: #cce5ff;";
    private static final String STYLE_SELECTED = "-fx-background-color: #7bd67b;";
    private static final String STYLE_BOOKED = "-fx-background-color: #cccccc;";

    private final SeatService seatService;
    private final Consumer<List<Seat>> onSeatsSelected;
    private final Runnable onBack;
    private final SeatSelection selection = new SeatSelection();

    public SeatController(SeatService seatService, Consumer<List<Seat>> onSeatsSelected, Runnable onBack) {
        this.seatService = seatService;
        this.onSeatsSelected = onSeatsSelected;
        this.onBack = onBack;
    }

    public Scene createScene(Show show) {
        List<Seat> seats = seatService.seatsForShow(show.getId());
        Map<Seat, ToggleButton> buttons = new HashMap<>();

        Button continueButton = new Button("Continue");
        continueButton.setDisable(true);
        continueButton.setOnAction(event -> onSeatsSelected.accept(selection.getSelected()));

        GridPane grid = new GridPane();
        grid.setHgap(6);
        grid.setVgap(6);
        int maxColumn = seats.stream().mapToInt(Seat::getColumn).max().orElse(0);
        for (Seat seat : seats) {
            ToggleButton cell = new ToggleButton(seat.getLabel());
            cell.setPrefSize(44, 32);
            if (!seat.isAvailable()) {
                cell.setDisable(true);
                cell.setStyle(STYLE_BOOKED);
            } else {
                cell.setStyle(STYLE_AVAILABLE);
                cell.setOnAction(event -> {
                    if (selection.toggle(seat)) {
                        cell.setStyle(selection.isSelected(seat) ? STYLE_SELECTED : STYLE_AVAILABLE);
                    }
                    continueButton.setDisable(selection.size() == 0);
                });
            }
            buttons.put(seat, cell);
            grid.add(cell, seat.getColumn() - 1, seat.getRow() - 'A');
        }
        grid.setAlignment(Pos.CENTER);

        Button back = new Button("Back");
        back.setOnAction(event -> onBack.run());

        HBox legend = new HBox(14,
                legendItem("Available", STYLE_AVAILABLE),
                legendItem("Selected", STYLE_SELECTED),
                legendItem("Booked", STYLE_BOOKED));
        legend.setAlignment(Pos.CENTER);

        Label title = new Label(show.getMovie().getTitle() + " — " + show.getTheatre().getName());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox root = new VBox(15, title, grid, legend, continueButton, back);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        return new Scene(root, 900, 640);
    }

    private HBox legendItem(String text, String style) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color: #efefef; -fx-padding: 4 10; " + style);
        return new HBox(label);
    }
}