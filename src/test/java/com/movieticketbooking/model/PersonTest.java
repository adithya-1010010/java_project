package com.movieticketbooking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(" ", "alice@example.com", null));
    }

    @Test
    void rejectsInvalidEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("Alice", "", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("Alice", "not-an-email", null));
    }

    @Test
    void acceptsValidPerson() {
        Customer customer = new Customer("Alice Johnson", "alice@example.com", "555-0100");

        assertEquals("Alice Johnson", customer.getFullName());
        assertEquals("alice@example.com", customer.getEmail());
        assertEquals("555-0100", customer.getPhone());
    }

    @Test
    void rejectsBlankUsername() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("Alice", "alice@example.com", null, " ", "hash"));
    }

    @Test
    void rejectsBlankPasswordHash() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("Alice", "alice@example.com", null, "alice", " "));
    }
}