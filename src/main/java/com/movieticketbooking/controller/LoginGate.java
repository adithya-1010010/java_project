package com.movieticketbooking.controller;

import com.movieticketbooking.model.User;
import com.movieticketbooking.service.AuthenticationService;

import java.util.Optional;
import java.util.function.Consumer;

public class LoginGate {

    private final AuthenticationService authenticationService;

    public LoginGate(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public boolean attempt(String username, String password, Consumer<User> onSuccess) {
        Optional<User> user = authenticationService.authenticate(username, password);
        if (user.isEmpty()) {
            return false;
        }
        onSuccess.accept(user.get());
        return true;
    }
}