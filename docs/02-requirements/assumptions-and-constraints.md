# Assumptions and Constraints

## Assumptions

- Single-user local desktop use; no concurrency beyond duplicate-seat race protection.
- Initial data (movies, theatres, shows, seats) is seeded into SQLite.
- Authentication is local and academic in scope (not production-grade security).
- `context.md` line "OOP Module — Uses classes, objects, inheritance, and polymorphism in JavaScript" is a typo; the project is Java. Recorded in `memory.md`.

## Constraints

- Java 17 language level, JavaFX UI, Maven build, SQLite persistence, JUnit 5 testing (confirmed stack; changes require a documented decision).
- One phase at a time, in order, per `implementation-plan.md`.
- No payment, VIP seats, ticket file output, or admin management UI.
- Dependencies added only when documented and justified.
