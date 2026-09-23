# Database Design

- SQLite local database (confirmed).
- Normalized relational design — no serialized booking blobs.
- Tables planned: users, movies, theatres, shows, seats, customers, bookings, booking_seats.
- **Duplicate-seat rule (REQ-08):** a (show_id, seat_id) combination must be unique in `booking_seats`; enforced by a DB unique constraint plus service-level re-check inside the booking transaction.
- Full schema/constraints: `schema.md` (Phase 03). Data movement: `data-flow.md`.

Authority: [implementation plan §8](../../implementation-plan.md).