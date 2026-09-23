# Engineering Memory

Source of truth. Every phase updates this file before its Git commit.

## Project

- **Name:** Movie Ticket Booking System
- **Repo:** https://github.com/adithya-1010010/java_project.git (branch `main`)
- **Type:** Single-user local JavaFX desktop app
- **Requirements source:** `context.md`

## Confirmed requirements

From `context.md` (see `docs/02-requirements/requirements.md` for IDs REQ-01…15):

- Automate ticket booking; browse movies; genres + prices; theatres + show timings; customer details; ticket total; duplicate-seat prevention; booking ID + ticket details on confirmation; OOP (class/object/inheritance/polymorphism).
- In scope for now: login required, seeded data, simple row/column seats, on-screen confirmation only.
- Out of scope: payment, VIP seats, admin UI, ticket file output, multi-user.

## Confirmed technical decisions

| Decision | Value |
|---|---|
| Language | Java 17 (language level) |
| UI | JavaFX 21.0.5 |
| Build | Maven 3.9.x |
| Persistence | SQLite (`sqlite-jdbc` 3.47.2.0) |
| DB access | JDBC (`java.sql`) |
| Testing | JUnit 5 (Jupiter 5.11.4) |
| Package root | `com.movieticketbooking` |
| App run | `mvn javafx:run` |

## Architecture decisions

- Simple layered architecture: UI → controllers → services → (domain, validation) → repository/DAO → JDBC → SQLite. Confirmed in `docs/04-architecture/architecture.md`.
- One phase at a time; phases from `implementation-plan.md` §11.
- Seat duplicate prevention on **two levels**: service validation AND DB uniqueness constraint on (show, seat) in a transaction (REQ-08).
- Normalized relational DB; no blobs.

## Environment / JVM note (important)

- Local machine had **no JDK 17 binary** — only user-facing OpenJDK 25 and Homebrew OpenJDK 26.
- Decision: keep language level **17** via `maven.compiler.release=17`; do not require a JDK 17 install. Not a plan deviation (plan language = Java 17); recorded for clarity.
- Maven was not installed; installed via Homebrew **3.9.16** on 2026-09-23.

## Class / design decisions

Finalized in Phase 02 (all under `com.movieticketbooking.model`; pricing under `model.pricing`):

- Entities: Movie (+ `MovieGenre`), Theatre, Show, Seat (+ `SeatState`), Person (abstract), Customer, User, Ticket, Booking.
- **Inheritance:** `Person` (abstract: fullName, email, phone, `describe()`) → `Customer`, `User`. Genuine is-a shared identity fields.
- **Polymorphism:** `PricingStrategy` interface → `StandardPricing`, `DiscountedPricing`. `Ticket` delegates total calculation (supports REQ-10).
- **Money:** all prices/totals are `BigDecimal`, normalized to 2 decimals (HALF_UP).
- **Model invariants:** price ≥ 0; show end after start; unique seats per show; booking needs ≥ 1 seat; `Booking.confirm()` re-checks availability then marks seat(s) booked (no partial confirm); duplicate seat booking rejected at model level.
- `Ticket` also validates seats belong to the show and no duplicates.

## DB design decisions

- Tables planned: users, movies, theatres, shows, seats, customers, bookings, booking_seats. Schema finalized in Phase 03.

## UI decisions

- Screens: Login, Home, Movie listing, Show selection, Seat grid, Customer details, Summary, Confirmation. Clean/understated; not over-engineered.

## Confirmed/discovered vs context.md

- `context.md` says "OOP Module — Uses classes, objects, inheritance, and polymorphism **in JavaScript**" — treated as a typo; project is Java.
- `context.md` "Theatre and show timings" management is scoped to customer-facing listing/selection initially.

## Completed / current / next

- Completed: **Phase 01** (foundation + docs). Commit `phase-01: initialize JavaFX Maven project and project documentation`.
- Completed: **Phase 02** (domain model + OOP foundation). Commit `phase-02: domain model and oop foundation`.
- Current: none in progress.
- Next: **Phase 03** — SQLite schema, seed data, persistence layer (do NOT start automatically; only on explicit instruction).

## Known limitations

- No genuine concurrency in the app; transaction + unique constraint cover the duplicate-seat race.
- Authentication is local/academic, not hardened security.

## Phase-01 verification

- `mvn test`: BUILD SUCCESS, 1 test, 0 failures.
- `mvn javafx:run`: window opened.

## Phase-02 verification

- `mvn test`: BUILD SUCCESS, 47 tests, 0 failures.
- `mvn package`: BUILD SUCCESS.
- Tests cover creation, relationships, invariants, polymorphic behavior, and duplicate-seat prevention (model level).

- All commands run on this machine before commit.