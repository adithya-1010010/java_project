# Classes

Placeholder — class catalog finalized in Phase 02 with the domain model.

Planned core classes (from implementation plan §6):

- Movie, Theatre, Show, Seat, Customer, Booking, Ticket, User/credential representation.

Likely relationships:

```text
Movie 1 ─── * Show
Theatre 1 ─── * Show
Show 1 ─── * Seat
Customer 1 ─── * Booking
Booking 1 ─── 1 Show
Booking 1 ─── * Seat
Booking 1 ─── 1 Ticket
User 1 ─── * Booking   (if booking is associated with authenticated user)
```

Do not add entities until a requirement or design need justifies them.
