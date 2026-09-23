# Phase 06 — Seat Management

## Goal

Implement the row/column seat grid and seat availability behavior.

## Delivered

- **`model.SeatSelection`** — pure selection model (toggle available, block booked, multi-select, deselect, clear).
- **`service.SeatService`** — `seatsForShow(showId)` loads the grid with DB state (BOOKED seats restored).
- **`controller.SeatController`** — JavaFX grid: each seat is a `ToggleButton`; unavailable seats disabled/greyed, available seats toggle blue↔green, legend, Continue disabled until a seat is selected.
- **`Main`** — show selection now navigates to the seat grid; Continue → placeholder (booking in Phase 07).

## Tests (JUnit 5) — 81 total, 0 failures

- `SeatSelectionTest` — available selectable, toggle deselects, booked/null rejected, multi-select, clear.
- `SeatServiceTest` — full layout (80, A1…H10), all available at first, booked state reflected after a transaction, unknown show empty.

## Verification

- `mvn test` → 81 tests, 0 failures.
- `mvn javafx:run` → app opens; movie → show → seat grid flow.

## Declared only (later phases)

Booking workflow (07), confirmation (08).

## Git

Commit: `phase-06: seat grid and availability`

## Completion

Phase 06 complete. **Continuing directly to Phase 07.**