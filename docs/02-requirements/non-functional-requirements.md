# Non-Functional Requirements

- **NFR-01** Build and run on a local machine with Java 17+ and Maven; no server required.
- **NFR-02** All booking data persists locally in a single SQLite database file.
- **NFR-03** UI remains visually clean and understandable; not over-engineered.
- **NFR-04** Automated JUnit 5 tests must pass before every phase commit.
- **NFR-05** Layered architecture boundaries (UI → controller → service → repository) must be respected.
- **NFR-06** No secrets hardcoded in source code (demo credentials seeded safely).
- **NFR-07** Seat booking must be atomic — no partial bookings after a failure.
