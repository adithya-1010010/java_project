package com.movieticketbooking.model;

public abstract class Person {

    private final String fullName;
    private final String email;
    private final String phone;

    protected Person(String fullName, String email, String phone) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("fullName must not be blank");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("email must be a valid email address");
        }
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public abstract String describe();
}