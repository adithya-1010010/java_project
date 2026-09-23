# Testing Strategy

- **Unit tests (JUnit 5)** for domain invariants, validation, calculation, and services — no UI required.
- **Persistence tests** against SQLite covering schema init, seed data, CRUD, restart survival, duplicate-seat constraint.
- **Manual verification** for JavaFX UI flows at each phase and full end-to-end walkthrough in Phase 09.
- **Rule:** never claim a test passed without running it.
- Evidence: `test-plan.md` and `test-cases.md`.