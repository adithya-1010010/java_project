package com.movieticketbooking.model;

public class Theatre {

    private final long id;
    private final String name;
    private final String location;

    public Theatre(long id, String name, String location) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}