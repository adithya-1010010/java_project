# Phase 09 — End-to-End Integration and Validation

## Goal

Verify the complete system as one application.

## Delivered

- **`integration.E2eFlowTest`** — walks the whole journey with the real SQLite DB:
  - `happyPathLoginBrowseSelectBookConfirm` — login → browse → show → grid → book → confirm, asserting code, confirmation, quantity, total;
  - `invalidLoginNeverEntersApplication`;
  - `invalidBookingInputsAreRejected` — null customer, empty seats, no partial record;
  - `duplicateSeatBookingIsPreventedAcrossSessions`;
  - `bookingSurvivesRestartAndStillBlocksDuplicateSeats` — persistence after restart + post-restart duplicate protection;
  - `emptyStatesAreHandled` — unknown movie/shows/seats.
- **`DatabaseInitTest.initFailureIsReportedWhenDatabaseCannotBeCreated`** — a blocked DB path surfaces a clear `IllegalStateException`.

## Verification

- `mvn test` → 98 tests, 0 failures.
- `mvn javafx:run` → app opens (manual UI smoke).
- Full automated flow passes against a fresh SQLite file, including restart.

## Git

Commit: `phase-09: integrate and validate complete booking flow`

## Completion

Phase 09 complete. **Continuing directly to Phase 10.**