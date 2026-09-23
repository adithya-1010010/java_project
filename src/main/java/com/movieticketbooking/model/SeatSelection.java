package com.movieticketbooking.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SeatSelection {

    private final Set<Seat> selected = new LinkedHashSet<>();

    public boolean toggle(Seat seat) {
        if (seat == null || !seat.isAvailable()) {
            return false;
        }
        if (!selected.remove(seat)) {
            selected.add(seat);
        }
        return true;
    }

    public boolean isSelected(Seat seat) {
        return selected.contains(seat);
    }

    public List<Seat> getSelected() {
        return List.copyOf(selected);
    }

    public int size() {
        return selected.size();
    }

    public void clear() {
        selected.clear();
    }
}