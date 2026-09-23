# Phase 07 — Booking and Ticket Calculation

## Goal

Implement the actual booking business logic: customer details, validation, ticket quantity/total, transactional persistence with an availability re-check.

## Delivered

- **`service.BookingService`** — `confirmBooking(customer, show, seats)`:
  - rejects missing/null customer details and seats from another show;
  - refreshes seat state from the DB and re-checks availability immediately before persistence;
  - computes `Ticket` via `StandardPricing` (quantity = seats, total = unit × count);
  - ends in a transactional `BookingRepository.create` (booking row + seat links + `state='BOOKED'` all-or-nothing), generating the `BK-…` booking ID.
- **`controller.CustomerController`** — name/email/phone form with inline validation.
- **`controller.BookingSummaryController`** — review: customer, movie, theatre, show time, seats, unit price, tickets, total.
- **`Main`** — seat → customer details → review → confirm → booking record; failure paths return to a fresh seat grid.

## Tests (JUnit 5) — 89 total, 0 failures

`BookingServiceTest`:
1. single-ticket booking persisted with `BK-` code and seat marked BOOKED;
2. multiple-ticket booking stores all seats in order;
3. total = unit price × quantity (correct cost);
4. missing customer details (null, blank name, invalid email) rejected;
5. no selected seats (empty/null) rejected;
6. duplicate seat fails at service pre-check (fresh reload);
7. stale in-memory seat loses the race at the final persistence layer → rollback, no partial record.

## Verification

- `mvn test` → 89 tests, 0 failures.
- `mvn javafx:run` → app opens; movie → show → seat → customer → review flow.

## Declared only (later phases)

Confirmation screen with full ticket details (Phase 08).

## Git

Commit: `phase-07: implement booking workflow and ticket calculation`

## Completion

Phase 07 complete. **Continuing directly to Phase 08.**