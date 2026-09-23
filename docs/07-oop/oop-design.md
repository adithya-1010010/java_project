# OOP Design

The system must demonstrate OOP for real design reasons — no artificial hierarchies.

## Class and objects

Domain entities (Movie, Show, Seat, Booking, Ticket, …) have clear state and behavior.

## Inheritance

Only where a genuine `is-a` relationship exists (candidate area: a ticket/booking presentation hierarchy — final decision in Phase 02).

## Polymorphism

Through meaningful abstractions: shared service/interface/repository contracts, or a real domain hierarchy. Never a parent class created solely to claim polymorphism.

The final class diagram must explicitly label where class, object, inheritance, and polymorphism are demonstrated.

Details: [classes.md](classes.md), [inheritance.md](inheritance.md), [polymorphism.md](polymorphism.md).
