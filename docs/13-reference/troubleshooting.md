# Troubleshooting

| Symptom | Fix |
|---|---|
| `mvn: command not found` | Install Maven: `brew install maven` |
| JavaFX window fails to open | Verify GUI session; run `mvn -B javafx:run` for logs |
| "invalid target release" | Confirm `maven.compiler.release=17` and a compatible JDK |
| Duplicate-seat test failures | Check `booking_seats` unique constraint + transaction re-check |

Updated as issues arise; see also `future-improvements.md`.