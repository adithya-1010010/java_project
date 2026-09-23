# Troubleshooting

| Symptom | Fix |
|---|---|
| `mvn: command not found` | Install Maven: `brew install maven` |
| JavaFX window fails to open | Verify GUI session; run `mvn -B javafx:run` for logs |
| "invalid target release" | Confirm `maven.compiler.release=17` and a compatible JDK |
| Duplicate-seat test failures | Check `booking_seats` unique constraint + transaction re-check |
| App can't start, DB path blocked | Clear/verify `~/.movieticket`; `Database.init()` reports `IllegalStateException` |
| Cross-show seat book test surprises | `Seat.equals` is position-aware (row+col); membership/availability must be checked by seat **id** in the service layer (see `BookingService`) |

Updated as issues arise; see also `future-improvements.md`.