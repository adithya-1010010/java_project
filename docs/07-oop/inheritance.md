# Inheritance

**Decision (Phase 02):** a `Person` abstract base class with subclasses `Customer` and `User`.

```text
       ┌──────────┐
       │  Person  │  abstract — fullName, email, phone; describe()
       └─────┬────┘
      ┌──────┴──────┐
      ▼             ▼
  Customer       User      + username, passwordHash
  ─────────      ─────────
  The booker     The authenticated account holder
```

## Why it is genuine `is-a`

- A **Customer** *is a* person who books tickets: it adds booker semantics to shared identity data.
- A **User** *is a* person with a login account: it adds credentials to the same shared identity data.
- Both override the abstract `describe()` (covered by OOP behavior tests in `OopDesignTest`).

## Why this design

- Real design reason: both roles carry a name, email, and phone, so the fields/logic live in one place instead of being duplicated.
- The plan rule "inheritance only for genuine is-a" is satisfied; no artificial parent was introduced.

No other inheritance is present. See [oop-design.md](oop-design.md) for the diagram.