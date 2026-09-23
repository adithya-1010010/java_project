package com.movieticketbooking.controller;

import com.movieticketbooking.model.Booking;
import com.movieticketbooking.model.Seat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class ConfirmationController {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("EEE d MMM yyyy, HH:mm");
    private static final DateTimeFormatter TIME_ONLY = DateTimeFormatter.ofPattern("HH:mm");

    private final Runnable onNewBooking;
    private final Runnable onHome;

    public ConfirmationController(Runnable onNewBooking, Runnable onHome) {
        this.onNewBooking = onNewBooking;
        this.onHome = onHome;
    }

    public Scene createScene(Booking booking) {
        Label header = new Label("Booking Confirmed");
        header.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2b7d2b;");

        Label code = new Label("Booking ID: " + booking.getBookingCode());
        code.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(8);
        int row = 0;
        details.addRow(row++, new Label("Customer:"), new Label(booking.getCustomer().getFullName()
                + " | " + booking.getCustomer().getEmail()
                + (booking.getCustomer().getPhone() == null || booking.getCustomer().getPhone().isEmpty()
                        ? "" : " | " + booking.getCustomer().getPhone())));
        details.addRow(row++, new Label("Movie:"), new Label(booking.getShow().getMovie().getTitle()
                + " | " + booking.getShow().getMovie().getGenre()));
        details.addRow(row++, new Label("Theatre:"), new Label(booking.getShow().getTheatre().getName()
                + " | " + booking.getShow().getTheatre().getLocation()));
        details.addRow(row++, new Label("Show time:"), new Label(TIME_FORMAT.format(booking.getShow().getStartTime())
                + " – " + TIME_ONLY.format(booking.getShow().getEndTime())));
        details.addRow(row++, new Label("Seats:"), new Label(
                booking.getSeats().stream().map(Seat::getLabel).reduce((a, b) -> a + ", " + b).orElse("none")));
        details.addRow(row++, new Label("Tickets:"), new Label(String.valueOf(booking.getTicket().getQuantity())));
        details.addRow(row++, new Label("Unit price:"), new Label("$" + booking.getTicket().getUnitPrice().toPlainString()));
        details.addRow(row++, new Label("Total:"), new Label("$" + booking.getTicket().getTotal().toPlainString()));
        details.addRow(row, new Label("Booked at:"), new Label(booking.getCreatedAt().format(TIME_FORMAT)));
        details.setAlignment(Pos.CENTER);

        Button newBooking = new Button("New Booking");
        newBooking.setOnAction(event -> onNewBooking.run());
        Button home = new Button("Back to Home");
        home.setOnAction(event -> onHome.run());

        HBox buttons = new HBox(12, newBooking, home);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(16, header, code, details, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));
        return new Scene(root, 760, 600);
    }
}