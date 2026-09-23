PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS users (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    username      TEXT    NOT NULL UNIQUE,
    password_hash TEXT    NOT NULL,
    full_name     TEXT    NOT NULL,
    email         TEXT    NOT NULL,
    phone         TEXT
);

CREATE TABLE IF NOT EXISTS movies (
    id                 INTEGER PRIMARY KEY,
    title              TEXT    NOT NULL,
    genre              TEXT    NOT NULL,
    ticket_price_cents INTEGER NOT NULL CHECK (ticket_price_cents >= 0),
    duration_minutes   INTEGER NOT NULL CHECK (duration_minutes > 0),
    synopsis           TEXT
);

CREATE TABLE IF NOT EXISTS theatres (
    id       INTEGER PRIMARY KEY,
    name     TEXT NOT NULL,
    location TEXT
);

CREATE TABLE IF NOT EXISTS shows (
    id         INTEGER PRIMARY KEY,
    movie_id   INTEGER NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
    theatre_id INTEGER NOT NULL REFERENCES theatres(id) ON DELETE CASCADE,
    start_time TEXT    NOT NULL,
    end_time   TEXT    NOT NULL,
    CHECK (end_time > start_time)
);

CREATE TABLE IF NOT EXISTS seats (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    show_id       INTEGER NOT NULL REFERENCES shows(id) ON DELETE CASCADE,
    row_label     TEXT    NOT NULL,
    column_number INTEGER NOT NULL,
    state         TEXT    NOT NULL DEFAULT 'AVAILABLE' CHECK (state IN ('AVAILABLE', 'BOOKED')),
    UNIQUE (show_id, row_label, column_number)
);

CREATE TABLE IF NOT EXISTS customers (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    full_name TEXT NOT NULL,
    email     TEXT NOT NULL,
    phone     TEXT
);

CREATE TABLE IF NOT EXISTS bookings (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    booking_code TEXT   NOT NULL UNIQUE,
    customer_id  INTEGER NOT NULL REFERENCES customers(id),
    show_id      INTEGER NOT NULL REFERENCES shows(id),
    total_cents  INTEGER NOT NULL CHECK (total_cents >= 0),
    quantity     INTEGER NOT NULL CHECK (quantity > 0),
    created_at   TEXT    NOT NULL
);

CREATE TABLE IF NOT EXISTS booking_seats (
    booking_id INTEGER NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
    seat_id    INTEGER NOT NULL REFERENCES seats(id) ON DELETE CASCADE,
    PRIMARY KEY (booking_id, seat_id),
    UNIQUE (seat_id)
);