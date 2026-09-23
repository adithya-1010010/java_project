# Phase 03 — SQLite Database and Persistence Foundation

## Goal

Create persistent storage and a clean database access layer, with schema, seed data, constraints, and repository/DAO foundations.

## Delivered

### Config
- `config.Database` — path/URL management, `open()` (enables `PRAGMA foreign_keys = ON`), `init()` running `schema.sql` + `seed.sql` resources.
- `config.Seeder` — fills seat grids (8×10 = 80 per show) and demo users idempotently.

### Schema & seed (resources)
- Tables: `users`, `movies`, `theatres`, `shows`, `seats`, `customers`, `bookings`, `booking_seats`.
- Constraints: FK enforcement, `seats UNIQUE(show_id, row_label, column_number)`, `booking_seats UNIQUE(seat_id)` (duplicate-seat protection), `end > start`, non-negative cents, positive quantity.
- Seed: 4 movies, 2 theatres, 4 shows; users `demo`/`demo123` and `alice`/`alice123` seeded with SHA-256 hashes (via `util.PasswordHasher`).

### Repositories (`com.movieticketbooking.repository`)
`MovieRepository`, `TheatreRepository`, `ShowRepository`, `SeatRepository` (read + grid insert + `markBooked`), `CustomerRepository`, `UserRepository`, `BookingRepository`.

`BookingRepository.create` is transactional: insert customer → insert booking (generated `BK-…` code, cents totals) → insert `booking_seats` → mark seats BOOKED → commit; rollback on any failure (no partial booking).

### Mapping
- `util.Money` (cents ↔ BigDecimal), `util.PasswordHasher` (SHA-256 hex).
- `Ticket.reconstruct` / `Booking.reconstruct` static factories added so persisted entities can be loaded back.

## Tests (JUnit 5) — 58 total, 0 failures

- `DatabaseInitTest` — tables created from clean state; seed loads (4 movies, 80 seats/show, demo users); init is idempotent; seat layout unique per show; FKs enforced; hashed passwords.
- `BookingPersistenceTest` — create + code; retrieve with full details; duplicate seat on same show rejected without partial record; same position on a different show allowed; data survives restart (reopen + re-init).

## Verification

- `mvn test` → 58 tests, 0 failures (BUILD SUCCESS)

## Declared only (later phases)

Services/controllers/UI, auth flows, movie/show/seat screens.

## Git

Commit: `phase-03: sqlite schema seed and persistence layer`

## Completion

Phase 03 complete. **STOP.** Do not start Phase 04.