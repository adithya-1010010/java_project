# Phase 04 — Local Authentication and Login Flow

## Goal

Implement the confirmed login requirement: local, single-user, academic scope.

## Delivered

- **`service.AuthenticationService`** — `authenticate(username, password)`:
  - rejects blank input;
  - looks up the user by username (`UserRepository`),
  - SHA-256 hashes the candidate password and compares constant-time against the stored hash;
  - returns the matched `User` or empty.
- **`controller.LoginGate`** — pure orchestration that "enters the application" (invokes `onSuccess`) only when authentication succeeds; returns `false` otherwise.
- **`controller.LoginController`** — JavaFX login scene (username, password, login button, error label); on failure shows "Invalid username or password." and never navigates.
- **`controller.HomeController`** — minimal home screen greeting the authenticated user (full browse UI is Phase 05).
- **`Main`** — wires DB init → auth → login scene → home on success; DB stored at `~/.movieticket/movieticket.db`.

## Credentials

Seeded demo accounts (from Phase 03, SHA-256 hashed, no plaintext in source):
`demo`/`demo123`, `alice`/`alice123`.

## Tests (JUnit 5) — 65 total, 0 failures

- `AuthenticationServiceTest` — valid credentials, wrong password, unknown user, blank/null fields.
- `LoginGateTest` — valid creds enter application; invalid creds / blank fields do **not** (consumer never invoked).

## Verification

- `mvn test` → 65 tests, 0 failures.
- `mvn javafx:run` → login window opens (`APPLICATION_STARTED`).
- Manual: valid credentials → home; invalid → error stays on login.

## Declared only (later phases)

Movie/show/seat/booking screens, customer form, confirmation.

## Git

Commit: `phase-04: local authentication and login flow`

## Completion

Phase 04 complete. **STOP.** Do not start Phase 05.