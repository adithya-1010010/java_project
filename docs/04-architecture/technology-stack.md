# Technology Stack

| Concern | Choice | Version | Rationale |
|---|---|---|---|
| Language | Java | 17 (language level) | Confirmed in implementation plan; compiled with `maven.compiler.release=17` |
| UI | JavaFX | 21.0.5 | Confirmed; desktop UI toolkit |
| Build | Maven | 3.9.x | Confirmed |
| Persistence | SQLite | via `sqlite-jdbc` 3.47.2.0 | Confirmed local DB |
| DB access | JDBC | JDK `java.sql` | Confirmed |
| Testing | JUnit 5 (Jupiter) | 5.11.4 | Confirmed |
| Run app | `mvn javafx:run` | javafx-maven-plugin 0.0.8 | Standard JavaFX run path |

## Environment notes

- Dev machine: macOS (aarch64), Homebrew Maven 3.9.16, JDK available to Maven is Homebrew OpenJDK (26) — bytecode still targets release 17.
- Only JDK 25 (user) / 26 (Homebrew) installed locally; **no JDK 17 binary present**. Decision: keep language level 17 via `--release 17`. Recorded in `memory.md`.

No other libraries may be added without a documented, justified decision in `memory.md`.
