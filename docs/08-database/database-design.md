# Database Design

- **Engine:** SQLite, single local file. Connection management in `config.Database` (`open()` + `PRAGMA foreign_keys = ON`).
- **Normalized relational design** — no serialized booking blobs.
- **Money:** integer cents (exact), mapped with `util.Money`.
- **Times:** ISO-8601 text (`LocalDateTime`), lexicographically sortable.
- **Schema scripts:** idempotent `schema.sql` + `seed.sql` resources run by `Database.init()`; Java `Seeder` adds seat grids and demo users.

## Tables

users, movies, theatres, shows, seats, customers, bookings, booking_seats — full DDL and constraints in [schema.md](schema.md).

## Duplicate-seat rule (REQ-08)

A seat may belong to at most one booking: `booking_seats.UNIQUE(seat_id)`. Combined with one seat row per (show, row, column), this makes **(show, seat) bookable once** — enforced by:

1. service/model validation (`Booking.confirm()` — rejects non-available seats);
2. database constraint inside the `BookingRepository.create` transaction (rollback on violation, no partial records).

See [data-flow.md](data-flow.md) for the transactional write path.