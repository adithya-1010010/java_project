package com.movieticketbooking.model;

public class User extends Person {

    private final String username;
    private final String passwordHash;

    public User(String fullName, String email, String phone, String username, String passwordHash) {
        super(fullName, email, phone);
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash must not be blank");
        }
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String describe() {
        return "User: " + username;
    }
}