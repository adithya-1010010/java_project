# Testing Strategy

- **Unit tests (JUnit 5)** for domain invariants, validation, calculation, and services — no UI required.
- **Persistence tests** against SQLite covering schema init, seed data, CRUD, restart survival, duplicate-seat constraint.
- **Integration test** (`E2eFlowTest`) driving the full login → browse → book → confirm journey against a real SQLite file.
- **Manual verification** for JavaFX UI flows at each phase.
- **Rule:** never claim a test passed without running it.
- Evidence: `test-plan.md` and `test-cases.md`. Final suite: 98 tests, 0 failures.