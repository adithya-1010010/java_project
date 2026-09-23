# Phase 02 — Domain Model and OOP Foundation

## Goal

Implement the core domain objects and the initial OOP design, without database dependence.

## Delivered

### Model (package `com.movieticketbooking.model`)
- `Movie`, `MovieGenre` (enum), `Theatre`
- `Show` (owns unique seat layout; available-seat query)
- `Seat`, `SeatState` (enum) — cannot be booked twice
- `Person` (abstract) → `Customer`, `User`
- `Ticket` — quantity, unit price, total via injected strategy
- `Booking` — confirm() re-checks availability then marks seats booked

### OOP
- **Inheritance:** `Person` → `Customer`, `User`.
- **Polymorphism:** `model.pricing.PricingStrategy` interface with `StandardPricing` / `DiscountedPricing`; delegated through `Ticket`.

### Money
- All prices/totals are `BigDecimal`, normalized to 2 decimal places (HALF_UP).

## Tests (JUnit 5)

New: `MovieTest`, `TheatreTest`, `SeatTest`, `ShowTest`, `PersonTest`, `TicketTest`, `BookingTest`, `PricingStrategyTest`, `OopDesignTest`, plus `TestFixtures` helper.

Coverage confirms the invalids branch: 47 tests, 0 failures:

- object creation and getters
- relationships (seat belongs to show, booking binds show+seats+ticket)
- invariants (blank/invalid fields, negative prices, end-before-start, duplicates)
- polymorphic behavior (Person.describe() by role; standard vs discounted totals)
- duplicate-seat prevention at model level (booking already-booked seats throws, confirm leaves state untouched)

## Verification

- `mvn test` → 47 tests, 0 failures (BUILD SUCCESS)
- `mvn package` → BUILD SUCCESS

## Declared only (later phases)

Persistence, services, auth, all UI — not part of this phase.

## Git

Commit: `phase-02: domain model and oop foundation`

## Completion

Phase 02 complete. **STOP.** Do not start Phase 03.