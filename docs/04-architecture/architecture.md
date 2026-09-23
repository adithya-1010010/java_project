# Architecture

## Style

Simple layered architecture with clear responsibility boundaries.

```text
                     JavaFX UI
                        │
                        ▼
                  Controllers
                        │
                        ▼
                    Services
                /       │       \
               /        │        \
              ▼         ▼         ▼
          Domain     Validation   DTO/View data
              │
              ▼
        Repository / DAO
              │
              ▼
            JDBC
              │
              ▼
           SQLite
```

## Package structure

```text
com.movieticketbooking
├── Main / Application entry point
├── config
├── controller
├── model
├── repository
├── service
├── validation
├── util
└── view
```

Package names may be finalized in Phase 01; responsibilities must remain separated.

## Responsibility rules

- **Model** — domain state and relationships.
- **Repository/DAO** — database access only.
- **Service** — business rules and orchestration.
- **Validation** — input/business validation that benefits from isolation and testing.
- **Controller** — translates JavaFX UI events into application operations and updates views.
- **View** — JavaFX screens and presentation.
- **Config/Util** — narrowly scoped infrastructure helpers; `Util` must not become a dumping ground.

## Dependencies

Dependencies flow inward/downward only: view → controller → service → (model, repository) → JDBC. No upward imports.

Related: [system-design.md](system-design.md), [component-design.md](component-design.md), [technology-stack.md](technology-stack.md).
