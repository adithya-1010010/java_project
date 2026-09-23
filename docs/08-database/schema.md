# Schema

SQLite, money stored as **integer cents**, timestamps as ISO-8601 text (`LocalDateTime.toString()`). Scripts: `src/main/resources/schema.sql`, `src/main/resources/seed.sql`.

## Tables

```sql
users       (id PK, username UNIQUE, password_hash, full_name, email, phone)
movies      (id PK, title, genre, ticket_price_cents CHECK(>=0), duration_minutes CHECK(>0), synopsis)
theatres    (id PK, name, location)
shows       (id PK, movie_id FK→movies, theatre_id FK→theatres,
             start_time, end_time, CHECK(end_time > start_time))
seats       (id PK, show_id FK→shows, row_label, column_number, state DEFAULT 'AVAILABLE',
             UNIQUE(show_id, row_label, column_number))
customers   (id PK, full_name, email, phone)
bookings    (id PK, booking_code UNIQUE, customer_id FK→customers, show_id FK→shows,
             total_cents CHECK(>=0), quantity CHECK(>0), created_at)
booking_seats (booking_id FK→bookings, seat_id FK→seats,
             PRIMARY KEY(booking_id, seat_id), UNIQUE(seat_id))
```

## Duplicate-seat protection (REQ-08) — DB level

A seat exists once per show (`seats.UNIQUE(show_id, row_label, column_number)`). Since `booking_seats.UNIQUE(seat_id)` allows a seat to appear in at most one booking, the combination **(show → seat) can never be booked twice**. Enforced inside the booking transaction; enforced at the model in `Booking.confirm()`.

## Seed data

- 4 movies, 2 theatres, 4 shows.
- Java seeder adds an 8×10 grid (rows A–H, cols 1–10; 80 seats per show) when a show has none.
- 2 demo users (`demo`/`demo123`, `alice`/`alice123`) seeded with SHA-256 hashed passwords via `util.PasswordHasher` — no plaintext secrets in source.
- Seeding is idempotent (`INSERT OR IGNORE` + existence checks).