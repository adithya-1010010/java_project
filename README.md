# Movie Ticket Booking System

A Java 17 / JavaFX desktop application for browsing movies, selecting shows and seats, and booking tickets with SQLite persistence.

## Status — Complete

All ten implementation phases are done and pushed to GitHub (`main`). Final suite: **98 JUnit 5 tests, 0 failures**.

Demo login: `demo` / `demo123` (or `alice` / `alice123`). Data lives at `~/.movieticket/movieticket.db`.

## Features

- Local authentication (SHA-256 hashed passwords, no plaintext)
- Browse movies with genre, ticket price, duration
- Select theatre and show timing
- Row/column seat grid — booked seats blocked, multi-select with deselect
- Customer details form with validation
- Booking summary with calculated total (ticket price × quantity)
- Transactional booking persistence — duplicate seats rejected at the service and database level
- Confirmation screen with booking ID and full ticket details
- Data survives application restart

## Requirements

- Java 17+ (`maven.compiler.release=17`; built and tested with modern JDKs)
- Maven 3.9+

## Build and Run

```bash
mvn test          # run JUnit 5 tests
mvn javafx:run    # launch the application
mvn -B clean test package   # full build + package
```

## User Flow

```text
Login → Home → Movie → Show → Seat grid → Customer details → Review/Summary → Confirmation
```

## OOP Concepts

Class, object, inheritance (`Person` → `Customer`/`User`), and polymorphism (`PricingStrategy` → `StandardPricing`/`DiscountedPricing`) are demonstrated in the domain model — see [docs/07-oop/](docs/07-oop/oop-design.md).

## Documentation

| Document | Purpose |
|---|---|
| [context.md](context.md) | Original project requirements |
| [Overview](docs/01-overview/overview.md) | Human-friendly project overview |
| [Requirements](docs/02-requirements/requirements.md) | Normalized requirement IDs |
| [Architecture](docs/04-architecture/architecture.md) | Approved layered architecture |
| [Technology stack](docs/04-architecture/technology-stack.md) | Confirmed technologies |
| [Implementation plan](docs/03-planning/implementation-plan.md) | Phase-by-phase execution blueprint |
| [Phases](docs/03-planning/phases.md) | Phase status tracking and commits |
| [Screens](docs/09-ui/screens.md) | Each screen's purpose, controls, validation, navigation |
| [Database design](docs/08-database/database-design.md) | Schema, money/times, duplicate-seat rule |
| [Test cases](docs/10-testing/test-cases.md) | Requirement-to-test traceability |
| [Memory](docs/13-reference/memory.md) | Persistent engineering memory (source of truth) |

Full docs live under [docs/](docs/) (overview, requirements, planning, architecture, flows, modules, OOP, database, UI, testing, development, phases, reference).

## Project Layout

```text
src/main/java/com/movieticketbooking/   application source
  ├── config/     Database, Seeder
  ├── controller/ JavaFX controllers
  ├── model/      domain model + pricing strategies
  ├── repository/ JDBC data access
  ├── service/    business logic
  └── util/       Money, PasswordHasher
src/main/resources/                     schema.sql, seed.sql
src/test/java/                          JUnit 5 tests
docs/                                   full documentation set
```