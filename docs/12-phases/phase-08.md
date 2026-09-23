# Phase 08 — Complete Confirmation / Ticket Module

## Goal

Complete the end-to-end user experience with a full confirmation screen and a clean reset for new bookings.

## Delivered

- **`controller.ConfirmationController`** — scene built from the persisted `Booking`:
  - Booking ID (code) highlighted;
  - customer, movie/genre, theatre, show time, seats, tickets quantity, unit price, total, booked-at timestamp;
  - **New Booking** → `Main.showMovies()` (fresh session); **Back to Home**.
- **`Main`** — successful `confirmBooking` now routes to confirmation; failed availability paths still return to a fresh seat grid in Phase 07.

## Tests (JUnit 5) — 91 total, 0 failures

`ConfirmationDataTest`:
1. `confirmationDataMatchesSavedBookingAcrossRestart` — after restart, the saved booking matches every value the confirmation screen shows (code, id, customer, movie, theatre, seats, quantity, prices).
2. `newBookingCanStartCleanlyAfterReopeningFreshGrid` — a second session books a different seat; DB reflects exactly two bookings with the expected BOOKED state.

## Verification

- `mvn test` → 91 tests, 0 failures.
- `mvn javafx:run` → app opens; seat → customer → review → confirm → confirmation flow.

## Declared only (later phases)

Full end-to-end run and validation (Phase 09).

## Git

Commit: `phase-08: complete booking confirmation and ticket display`

## Completion

Phase 08 complete. **Continuing directly to Phase 09.**