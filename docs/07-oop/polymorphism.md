# Polymorphism

**Decision (Phase 02):** a `PricingStrategy` interface with two implementations, delegated through `Ticket`.

```text
            ┌───────────────────┐
            │  PricingStrategy  │  interface — price(basePrice, ticketCount)
            └─────────┬─────────┘
           ┌──────────┴──────────┐
           ▼                     ▼
   StandardPricing       DiscountedPricing
   base × count          discount % once minTickets reached
```

## How it works

- `Ticket` takes a `PricingStrategy` and calls `price(unitPrice, quantity)` without knowing the concrete type.
- The same `Ticket` code produces different totals depending on the injected strategy (proved by `PricingStrategyTest` and `OopDesignTest`).
- This supports requirement **REQ-10** (calculate total ticket cost) and is an academic but real pricing model: standard pricing, and a group discount once a minimum number of tickets is bought.

## Why this design

- Real design reason: pricing rules vary and are isolated behind one contract instead of if/else inside `Ticket`.
- The plan rule "never a parent class created solely to claim polymorphism" is satisfied.