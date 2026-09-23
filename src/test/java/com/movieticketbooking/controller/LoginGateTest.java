package com.movieticketbooking.controller;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.User;
import com.movieticketbooking.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginGateTest {

    @TempDir
    Path dir;

    private LoginGate gate;

    @BeforeEach
    void setUp() {
        Database database = new Database(dir.resolve("login.db").toString());
        database.init();
        gate = new LoginGate(new AuthenticationService(database));
    }

    @Test
    void validCredentialsEnterTheApplication() {
        AtomicReference<User> entered = new AtomicReference<>();

        boolean success = gate.attempt("demo", "demo123", entered::set);

        assertTrue(success);
        assertEquals("demo", entered.get().getUsername());
    }

    @Test
    void invalidCredentialsDoNotEnterTheApplication() {
        AtomicReference<User> entered = new AtomicReference<>();

        boolean success = gate.attempt("demo", "wrong", entered::set);

        assertFalse(success);
        assertNull(entered.get());
    }

    @Test
    void blankCredentialsDoNotEnterTheApplication() {
        AtomicReference<User> entered = new AtomicReference<>();

        assertFalse(gate.attempt("", "", entered::set));
        assertFalse(gate.attempt("demo", " ", entered::set));
        assertNull(entered.get());
    }
}