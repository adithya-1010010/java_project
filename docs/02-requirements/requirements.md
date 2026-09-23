# Requirements

Normalized requirement IDs traced to phases. Detail: [functional-requirements.md](functional-requirements.md), [non-functional-requirements.md](non-functional-requirements.md), [assumptions-and-constraints.md](assumptions-and-constraints.md).

| ID | Requirement | Module | Implementation | Phase | Tests |
|---|---|---|---|---|---|
| REQ-01 | User must log in before using the application | Auth | Auth service + login UI | 04 | Auth tests |
| REQ-02 | Browse available movies | Movie | Movie repository/service + UI | 05 | Movie tests |
| REQ-03 | View genres and ticket prices | Movie | Movie model/service/UI | 05 | Movie tests |
| REQ-04 | Manage/view theatres and show timings | Show | Show repository/service/UI | 05 | Show tests |
| REQ-05 | Select a show for a movie | Booking/Show | Show selection UI | 05 | Show tests |
| REQ-06 | Display seats as a row/column grid | Seat | Seat service + grid UI | 06 | Seat tests |
| REQ-07 | Prevent selection of unavailable seats | Seat | Seat availability service/UI | 06 | Seat tests |
| REQ-08 | Prevent duplicate seat bookings (same show) | Seat/Booking | Service validation + DB uniqueness constraint/transaction | 06–07 | Seat/booking tests |
| REQ-09 | Enter and validate customer details | Booking | Booking form/validation | 07 | Validation tests |
| REQ-10 | Calculate total ticket cost | Ticket | Booking/ticket service | 07 | Calculation tests |
| REQ-11 | Persist booking in SQLite; survive restart | Booking | Booking repository + transaction | 07 | Persistence tests |
| REQ-12 | Generate and display a booking ID | Confirmation | Booking service | 07–08 | Booking tests |
| REQ-13 | Display complete ticket details on confirmation | Confirmation | Confirmation view | 08 | Integration tests |
| REQ-14 | Demonstrate class, object, inheritance, polymorphism | OOP | Domain/application design | 02 onward | Domain/OOP tests |
| REQ-15 | Home module provides main page and navigation | Home | Home view/controller | 05 | Integration tests |

Traceability must be kept current here and in `docs/10-testing/test-cases.md`.

Note: REQ-14 is implemented for Phase 02 (Person → Customer/User inheritance; PricingStrategy polymorphism, both with OOP tests).
