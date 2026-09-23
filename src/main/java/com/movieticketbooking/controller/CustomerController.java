package com.movieticketbooking.controller;

import com.movieticketbooking.model.Customer;
import com.movieticketbooking.model.Seat;
import com.movieticketbooking.model.Show;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.BiConsumer;

public class CustomerController {

    private final BiConsumer<Customer, List<Seat>> onCustomerEntered;
    private final Runnable onBack;

    public CustomerController(BiConsumer<Customer, List<Seat>> onCustomerEntered, Runnable onBack) {
        this.onCustomerEntered = onCustomerEntered;
        this.onBack = onBack;
    }

    public Scene createScene(Show show, List<Seat> seats) {
        TextField name = new TextField();
        name.setPromptText("Full name");
        TextField email = new TextField();
        email.setPromptText("Email address");
        TextField phone = new TextField();
        phone.setPromptText("Phone (optional)");

        Label error = new Label();
        error.setStyle("-fx-text-fill: red;");

        Button continueButton = new Button("Review Booking");
        continueButton.setOnAction(event -> {
            try {
                Customer customer = new Customer(name.getText(), email.getText(), phone.getText().trim());
                error.setText("");
                onCustomerEntered.accept(customer, seats);
            } catch (IllegalArgumentException e) {
                error.setText(e.getMessage());
            }
        });

        Button back = new Button("Back to Seats");
        back.setOnAction(event -> onBack.run());

        Label title = new Label("Enter Customer Details — " + show.getMovie().getTitle());
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label seatInfo = new Label("Selected seats: " +
                seats.stream().map(Seat::getLabel).reduce((a, b) -> a + ", " + b).orElse("none"));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Full name:"), 0, 0);
        form.add(name, 1, 0);
        form.add(new Label("Email:"), 0, 1);
        form.add(email, 1, 1);
        form.add(new Label("Phone:"), 0, 2);
        form.add(phone, 1, 2);
        form.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(10, continueButton, back);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, title, seatInfo, form, error, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        return new Scene(root, 700, 480);
    }
}