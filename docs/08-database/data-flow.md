# Data Flow

## Initialization

`config.Database.init()` runs on application start:

1. `schema.sql` — `CREATE TABLE IF NOT EXISTS` for all tables (idempotent).
2. `seed.sql` — catalog `INSERT OR IGNORE` (movies, theatres, shows).
3. `config.Seeder` — fills seat grids (80 per show) and demo users when missing.

## Reads (repositories)

```text
MovieRepository.findAll/findById        → movies
TheatreRepository.findAll/findById      → theatres
ShowRepository.findAll/findByMovieId    → shows (with movie + theatre)
SeatRepository.findByShow/findById      → seats (+ state)
CustomerRepository.findById             → customers
UserRepository.findByUsername           → users
BookingRepository.findByBookingCode     → full booking (customer + show + seats + ticket)
```

## Writes

`BookingRepository.create(Booking)` is **transactional**:

```text
BEGIN
  insert customer                (or reuse existing)
  insert booking (code generated, totals in cents)
  insert booking_seats rows      (UNIQUE(seat_id) → duplicate seat fails here)
  update seats → state = 'BOOKED'
COMMIT  |  ROLLBACK on any failure (no partial booking)
```

## Money / time mapping

- Cents ↔ `BigDecimal` via `util.Money` (`BigDecimal.valueOf(1200,2)` = 12.00).
- `LocalDateTime` ↔ ISO text via `toString()` / `parse()`, sortable lexicographically.
- Seat state restored by calling `seat.book()` for BOOKED rows (forward-only mapping).

## Persistence model

Single SQLite file at the configured path; every repository works against an `open()` connection from `config.Database` (which always enables `PRAGMA foreign_keys = ON`).