# Movie Ticket Booking System

A Java 17 / JavaFX desktop application for browsing movies, selecting shows and seats, and booking tickets with SQLite persistence.

## Status

Phase 07 complete — booking workflow and ticket calculation (Phases 01–07 done). See [implementation plan](docs/03-planning/implementation-plan.md) and [phase status](docs/03-planning/phases.md).

Demo login: `demo` / `demo123` (or `alice` / `alice123`).

## Requirements

- Java 17+ (built with `--release 17`)
- Maven 3.9+

## Build and Run

```bash
mvn test          # run JUnit 5 tests
mvn javafx:run    # launch the application
```

## Documentation

| Document | Purpose |
|---|---|
| [context.md](context.md) | Original project requirements |
| [Overview](docs/01-overview/overview.md) | Human-friendly project overview |
| [Requirements](docs/02-requirements/requirements.md) | Normalized requirement IDs |
| [Architecture](docs/04-architecture/architecture.md) | Approved layered architecture |
| [Technology stack](docs/04-architecture/technology-stack.md) | Confirmed technologies |
| [Implementation plan](docs/03-planning/implementation-plan.md) | Phase-by-phase execution blueprint |
| [Phases](docs/03-planning/phases.md) | Phase status tracking |
| [Memory](docs/13-reference/memory.md) | Persistent engineering memory (source of truth) |

## Project Layout

```text
src/main/java/com/movieticketbooking/   application source
src/main/resources/                     JavaFX resources
src/test/java/                          JUnit 5 tests
docs/                                   full documentation set
```

## OOP Concepts

Class, object, inheritance, and polymorphism are demonstrated in the domain model (see [docs/07-oop/](docs/07-oop/oop-design.md)).
