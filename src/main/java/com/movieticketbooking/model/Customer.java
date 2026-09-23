package com.movieticketbooking.model;

public class Customer extends Person {

    public Customer(String fullName, String email, String phone) {
        super(fullName, email, phone);
    }

    @Override
    public String describe() {
        return "Customer: " + getFullName();
    }
}