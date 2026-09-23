# Test Cases

Requirement-to-test traceability is kept current here and in `docs/02-requirements/requirements.md`.

| Req | Test cases | Status |
|---|---|---|
| REQ-01 | Valid / invalid / empty credentials; no entry on failed login | ✅ Done (Phase 04) |
| REQ-02/03 | Movies load; metadata correct; empty state handled | ✅ Done (Phase 05) |
| REQ-04/05 | Shows filtered by movie; invalid state handled | ✅ Done (Phase 05) |
| REQ-06/07 | Correct grid; available selectable; booked blocked | ✅ Done (Phase 06) |
| REQ-08 | Duplicate seat rejected at service + DB level | ✅ Done (Phase 07) |
| REQ-09 | Missing/invalid customer details | ✅ Done (Phase 07) |
| REQ-10 | Single + multiple ticket totals correct | ✅ Done (Phase 07) |
| REQ-11 | Persisted; survives restart; no partial booking | ✅ Done (Phase 07) |
| REQ-12 | Booking ID generated and returned | ✅ Done (Phase 07); display Phase 08 |
| REQ-13 | Confirmation matches booking | Phase 08 |
| REQ-14 | Domain/OOP behavior tests | ✅ Done (Phase 02) |
| REQ-15 | Navigation from home | Phase 05/09 |