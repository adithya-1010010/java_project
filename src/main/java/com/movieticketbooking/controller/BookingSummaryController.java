package com.movieticketbooking.controller;

import com.movieticketbooking.model.Customer;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.Show;
import com.movieticketbooking.model.Ticket;
import com.movieticketbooking.model.pricing.StandardPricing;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingSummaryController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("EEE d MMM yyyy, HH:mm");
    private static final DateTimeFormatter TIME_ONLY = DateTimeFormatter.ofPattern("HH:mm");

    private final Runnable onConfirm;
    private final Runnable onBack;

    public BookingSummaryController(Runnable onConfirm, Runnable onBack) {
        this.onConfirm = onConfirm;
        this.onBack = onBack;
    }

    public Scene createScene(Show show, Customer customer, List<Seat> seats) {
        Ticket ticket = new Ticket(show, seats, show.getMovie().getTicketPrice(), new StandardPricing());

        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(8);
        int row = 0;
        details.addRow(row++, new Label("Customer:"), new Label(customer.getFullName() + " | " + customer.getEmail()
                + (customer.getPhone() == null || customer.getPhone().isEmpty() ? "" : " | " + customer.getPhone())));
        details.addRow(row++, new Label("Movie:"), new Label(show.getMovie().getTitle() + " | " + show.getMovie().getGenre()));
        details.addRow(row++, new Label("Theatre:"), new Label(show.getTheatre().getName() + " | " + show.getTheatre().getLocation()));
        details.addRow(row++, new Label("Show time:"), new Label(TIME_FORMAT.format(show.getStartTime())
                + " – " + TIME_ONLY.format(show.getEndTime())));
        details.addRow(row++, new Label("Seats:"), new Label(seatList(seats)));
        details.addRow(row++, new Label("Unit price:"), new Label("$" + ticket.getUnitPrice().toPlainString()));
        details.addRow(row++, new Label("Tickets:"), new Label(String.valueOf(ticket.getQuantity())));
        details.addRow(row++, new Label("Total:"), new Label("$" + ticket.getTotal().toPlainString()));
        details.setAlignment(Pos.CENTER);

        Label title = new Label("Review Booking");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button confirm = new Button("Confirm Booking");
        confirm.setOnAction(event -> onConfirm.run());
        Button back = new Button("Back");
        back.setOnAction(event -> onBack.run());

        HBox buttons = new HBox(10, confirm, back);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(18, title, details, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        return new Scene(root, 760, 560);
    }

    private String seatList(List<Seat> seats) {
        return seats.stream().map(Seat::getLabel).reduce((a, b) -> a + ", " + b).orElse("none");
    }
}