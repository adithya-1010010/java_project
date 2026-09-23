# Implementation Plan (Copy in Docs)

The authoritative implementation blueprint lives at the repository root: [`implementation-plan.md`](../../implementation-plan.md).

This file is the in-docs location required by the documentation structure. If the two ever diverge, the root file is the operational contract and the conflict must be recorded in [`memory.md`](../13-reference/memory.md).

## Phases at a glance

1. Project foundation and documentation
2. Domain model and OOP foundation
3. SQLite database and persistence foundation
4. Authentication
5. Movie and show modules
6. Seat management
7. Booking and ticket calculation
8. Complete confirmation / ticket module
9. End-to-end integration and validation
10. Quality, polish, documentation, final acceptance

## Phase completion protocol

Read sources → implement only the current phase → test → review → document → update `memory.md` → update phase status → final build/test → commit → push → **STOP**.
