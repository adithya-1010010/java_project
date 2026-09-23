# Phase 05 — Movie and Show Modules

## Goal

Make the core catalog usable: browse movies (with genre + price), view shows per movie, navigate movie → show.

## Delivered

### Services
- `service.MovieService` — `listMovies()`, `findById(id)`.
- `service.ShowService` — `showsForMovie(movieId)` (each show carries its movie + theatre).

### UI
- `controller.MovieListController` — movie ListView (title | genre | $price | duration), empty state, Select + Back buttons.
- `controller.ShowController` — show ListView for a selected movie (theatre + location | start time), empty state.
- `controller.HomeController` — now has a "Browse Movies" button.
- `controller.PlaceholderController` — temporary "seat selection coming next phase" screen wired from show selection.
- **`Main`** — full navigation flow:

```text
Login → Home → Movie list → Show list → (placeholder for seat selection)
```

## Tests (JUnit 5) — 71 total, 0 failures

- `MovieServiceTest` — seeded movies load (4), metadata correct, find by id, missing id → empty.
- `ShowServiceTest` — shows filtered by movie (Inception → 2, The Godfather → none), theatre/timing present, unknown movie → empty.

## Verification

- `mvn test` → 71 tests, 0 failures.
- `mvn javafx:run` → app opens (login window), navigation usable.

## Declared only (later phases)

Seat grid (06), booking workflow (07), confirmation (08).

## Git

Commit: `phase-05: movie and show modules`

## Completion

Phase 05 complete. **Continuing directly to Phase 06** (per project instruction, phases proceed by default).