# Project Overview

## What is it?

The **Movie Ticket Booking System** is a Java-based desktop application that automates the traditional movie ticket booking process: browse movies, select show timings, choose seats, enter customer details, and receive a confirmed booking with a booking ID.

## Problem it solves

Manual ticket booking is slow and error-prone. This system makes the process faster, more efficient, and — critically — prevents duplicate seat bookings for the same show.

## What can the user do?

- Log in with local credentials
- Browse available movies with genres and ticket prices
- Select a theatre and show timing
- Pick seats on a row/column grid (booked seats are blocked)
- Enter customer details and review the booking
- See the calculated total cost
- Receive a booking ID and full ticket confirmation

## Technology

| Concern | Choice |
|---|---|
| Language | Java 17 |
| UI | JavaFX |
| Build | Maven |
| Database | SQLite (JDBC) |
| Testing | JUnit 5 |
| Scope | Single-user local desktop app |

## Structure

Simple layered architecture: **JavaFX UI → Controllers → Services → Domain/Validation → Repository/DAO → JDBC → SQLite**.

Packages live under `com.movieticketbooking` (`config`, `controller`, `model`, `repository`, `service`, `validation`, `util`, `view`).

## Major modules

Home, Movie, Booking, Ticket, Confirmation — plus the OOP design module.

## OOP concepts

- **Class / Object** — domain entities (Movie, Show, Seat, Booking, Ticket) with state and behavior.
- **Inheritance** — a genuine `is-a` hierarchy where requirements justify it (confirmed in Phase 02).
- **Polymorphism** — shared service/repository contracts and/or a real domain hierarchy (confirmed in Phase 02).

## Implementation phases

| Phase | Focus | Status |
|---|---|---|
| 01 | Project foundation and documentation | ✅ Complete |
| 02 | Domain model and OOP foundation | ⬜ Not started |
| 03 | SQLite database and persistence | ⬜ Not started |
| 04 | Authentication | ⬜ Not started |
| 05 | Movie and show modules | ⬜ Not started |
| 06 | Seat management | ⬜ Not started |
| 07 | Booking and ticket calculation | ⬜ Not started |
| 08 | Confirmation / ticket display | ⬜ Not started |
| 09 | End-to-end integration | ⬜ Not started |
| 10 | Quality, polish, final acceptance | ⬜ Not started |

Current status and decisions: [memory.md](../13-reference/memory.md). Full blueprint: [implementation-plan.md](../03-planning/implementation-plan.md).
