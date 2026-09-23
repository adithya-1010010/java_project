package com.movieticketbooking.model;

import java.util.Objects;

public class Seat {

    private final long id;
    private final char row;
    private final int column;
    private SeatState state;

    public Seat(long id, char row, int column) {
        if (!Character.isLetter(row)) {
            throw new IllegalArgumentException("row must be a letter");
        }
        if (column <= 0) {
            throw new IllegalArgumentException("column must be positive");
        }
        this.id = id;
        this.row = Character.toUpperCase(row);
        this.column = column;
        this.state = SeatState.AVAILABLE;
    }

    public long getId() {
        return id;
    }

    public char getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public SeatState getState() {
        return state;
    }

    public boolean isAvailable() {
        return state == SeatState.AVAILABLE;
    }

    public boolean book() {
        if (state == SeatState.BOOKED) {
            return false;
        }
        state = SeatState.BOOKED;
        return true;
    }

    public String getLabel() {
        return Character.toString(row) + column;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Seat seat)) {
            return false;
        }
        return row == seat.row && column == seat.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}