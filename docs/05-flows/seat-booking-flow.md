# Seat Booking Flow

```mermaid
flowchart TD
    A[Open Show] --> B[Load Seats for Show]
    B --> C[Display Grid]
    C --> D[User Selects Seat]
    D --> E{Seat Available?}
    E -->|No| F[Reject Selection]
    E -->|Yes| G[Mark Selected in UI]
    G --> H[Review Booking]
    H --> I[Re-check Availability]
    I -->|Unavailable| C
    I -->|Available| J[Create Booking Transaction]
    J --> K[Persist Booking + Seat Links]
```

Duplicate protection: service validation **and** DB uniqueness constraint on (show, seat) inside the transaction. Exact constraint documented in `docs/08-database/schema.md` (Phase 03).
