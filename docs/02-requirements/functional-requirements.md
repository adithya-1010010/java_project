# Functional Requirements

- **FR-01** The user must authenticate with valid local credentials before accessing application features (REQ-01).
- **FR-02** The system shall display the list of available movies (REQ-02).
- **FR-03** Each movie shall show its genre and ticket price (REQ-03).
- **FR-04** The system shall manage theatres and show timings (REQ-04).
- **FR-05** The user shall select a show associated with the chosen movie (REQ-05).
- **FR-06** The system shall display seats for the selected show as a row/column grid (REQ-06).
- **FR-07** Seats that are unavailable (booked) shall be visually distinguished and not selectable (REQ-07).
- **FR-08** A seat shall not be bookable twice for the same show; enforced by service validation and a database uniqueness constraint within a transaction (REQ-08).
- **FR-09** The user shall enter customer details (name, contact) with validation for missing/invalid input (REQ-09).
- **FR-10** The system shall calculate total cost = ticket price × number of selected seats (REQ-10).
- **FR-11** A confirmed booking shall be persisted in SQLite and survive application restart (REQ-11).
- **FR-12** The system shall generate a unique booking ID for each confirmed booking (REQ-12).
- **FR-13** A confirmation screen shall display booking ID, movie/show/theatre details, seats, customer details, quantity, and total cost (REQ-13).
- **FR-14** The home module shall provide the main page and navigation (REQ-15).

Original wording lives in `context.md`; any conflict is recorded in `docs/13-reference/memory.md`.
