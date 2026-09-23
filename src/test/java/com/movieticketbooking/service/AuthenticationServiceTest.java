package com.movieticketbooking.service;

import com.movieticketbooking.config.Database;
import com.movieticketbooking.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationServiceTest {

    @TempDir
    Path dir;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        Database database = new Database(dir.resolve("auth.db").toString());
        database.init();
        authenticationService = new AuthenticationService(database);
    }

    @Test
    void authenticatesValidCredentials() {
        Optional<User> user = authenticationService.authenticate("demo", "demo123");

        assertTrue(user.isPresent());
        assertEquals("demo", user.get().getUsername());
        assertEquals("Demo User", user.get().getFullName());
    }

    @Test
    void rejectsWrongPassword() {
        assertTrue(authenticationService.authenticate("demo", "wrong").isEmpty());
    }

    @Test
    void rejectsUnknownUser() {
        assertTrue(authenticationService.authenticate("nobody", "demo123").isEmpty());
    }

    @Test
    void rejectsBlankCredentials() {
        assertTrue(authenticationService.authenticate("", "").isEmpty());
        assertTrue(authenticationService.authenticate("demo", " ").isEmpty());
        assertTrue(authenticationService.authenticate("demo", null).isEmpty());
        assertTrue(authenticationService.authenticate(null, "demo123").isEmpty());
    }
}