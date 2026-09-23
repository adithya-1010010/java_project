# Ticket Module

- **Purpose:** Calculate total ticket cost based on number of tickets.
- **Status:** Implemented (Phase 07).
- **Requirements:** REQ-10.
- **Rule:** total = ticket price of show's movie × number of selected seats (via `PricingStrategy`, standard = linear).
- **Components:** calculation in `Ticket` domain object + `BookingService`; unit-tested (`BookingServiceTest`, `PricingStrategyTest`).
