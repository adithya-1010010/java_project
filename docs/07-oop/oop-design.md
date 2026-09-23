# OOP Design

The system demonstrates OOP for real design reasons — no artificial hierarchies.

```mermaid
classDiagram
    class Person {
        <<abstract>>
        +String fullName
        +String email
        +String phone
        +describe()*
    }
    class Customer {
        +describe()
    }
    class User {
        +String username
        +String passwordHash
        +describe()
    }
    Person <|-- Customer : inheritance
    Person <|-- User : inheritance

    class Movie {
        +String title
        +MovieGenre genre
        +BigDecimal ticketPrice
        +int durationMinutes
    }
    class Theatre {
        +String name
        +String location
    }
    class Show {
        +LocalDateTime startTime
        +LocalDateTime endTime
        +hasSeat(Seat)
        +getAvailableSeats()
    }
    class Seat {
        +char row
        +int column
        +SeatState state
        +book()
        +isAvailable()
    }
    class Booking {
        +String bookingCode
        +boolean confirmed
        +confirm()
    }
    class Ticket {
        +int quantity
        +BigDecimal unitPrice
        +BigDecimal total
    }
    class PricingStrategy {
        <<interface>>
        +price(basePrice, ticketCount) BigDecimal
    }
    class StandardPricing {
        +price(basePrice, ticketCount)
    }
    class DiscountedPricing {
        +int minTickets
        +BigDecimal discountPercent
        +price(basePrice, ticketCount)
    }
    PricingStrategy <|.. StandardPricing : polymorphism
    PricingStrategy <|.. DiscountedPricing : polymorphism

    Movie "1" --> "*" Show
    Theatre "1" --> "*" Show
    Show "1" --> "*" Seat
    Customer "1" --> "*" Booking
    Booking "1" --> "1" Show
    Booking "1" --> "*" Seat
    Booking "1" --> "1" Ticket
    Ticket "1" --> "1" PricingStrategy : uses
```

## Where each OOP concept is demonstrated

| Concept | Demonstration | Rationale |
|---|---|---|
| Class | Movie, Theatre, Show, Seat, Customer, User, Booking, Ticket | Domain entities with state and behavior |
| Object | Instances of every class, composed into shows/bookings | Objects interact through behavior, e.g. `Booking.confirm()` marks `Seat`s booked |
| Inheritance | `Person` (abstract) → `Customer`, `User` | Genuine `is-a`: both are people sharing identity/contact fields; each refines `describe()` |
| Polymorphism | `PricingStrategy` interface with `StandardPricing`, `DiscountedPricing`; delegated through `Ticket` | `Ticket` calls a strategy without knowing its type; runtime choice changes totals (REQ-10) |

Detail: [classes.md](classes.md), [inheritance.md](inheritance.md), [polymorphism.md](polymorphism.md).