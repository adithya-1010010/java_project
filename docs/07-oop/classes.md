# Classes

Finalized in Phase 02. All classes live under `com.movieticketbooking.model` (pricing strategies under `model.pricing`).

## Entity catalog

| Class | Role |
|---|---|
| `Movie` | Film with title, genre, price, duration, synopsis |
| `MovieGenre` (enum) | ACTION, COMEDY, DRAMA, SCI_FI, THRILLER, HORROR, ROMANCE, ANIMATION |
| `Theatre` | Auditorium with name and location |
| `Show` | A movie at a theatre at a time, owning its seat layout |
| `Seat` | Position (row/column) with `SeatState` (AVAILABLE, BOOKED) |
| `Person` (abstract) | Shared identity/contact base (fullName, email, phone) + abstract `describe()` |
| `Customer` | A person who books tickets |
| `User` | An authenticated account holder (username, passwordHash) |
| `Ticket` | Quantity, unit price, total for a set of seats on a show |
| `Booking` | Links customer + show + seats + ticket; `confirm()` marks seats booked |
| `pricing.PricingStrategy` (interface) | `price(basePrice, ticketCount)` contract |
| `pricing.StandardPricing` | unit × count |
| `pricing.DiscountedPricing` | percentage discount once a minimum ticket count is reached |

## Relationships

```text
Movie 1 ─── * Show
Theatre 1 ─── * Show
Show 1 ─── * Seat
Person (abstract) ──|→ Customer, User     (inheritance)
Customer 1 ─── * Booking
Booking 1 ─── 1 Show
Booking 1 ─── * Seat
Booking 1 ─── 1 Ticket
Ticket ──|→ PricingStrategy (StandardPricing, DiscountedPricing)   (polymorphism)
```

## Domain invariants (enforced in constructors/behavior)

- `Movie`: title non-blank, genre non-null, price ≥ 0, duration > 0.
- `Theatre`: name non-blank.
- `Show`: movie/theatre non-null, end after start, seat layout has no duplicate positions.
- `Seat`: row is a letter, column ≥ 1; cannot be booked twice.
- `Person` subclasses: name non-blank, valid email; `User` username/passwordHash non-blank.
- `Ticket`: non-empty seats, all seats belong to the show, no duplicate positions, non-null strategy and price.
- `Booking`: non-empty seats, seats belong to the show, no duplicates; `confirm()` re-checks availability before marking booked.

No unit was added without a requirement or design need.