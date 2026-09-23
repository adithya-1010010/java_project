# Phase 01 — Project Foundation and Documentation

## Goal

Create the Maven Java 17 JavaFX project skeleton and establish the engineering/documentation foundation.

## Work delivered

- `pom.xml`: Java 17 (`maven.compiler.release=17`), JavaFX 21.0.5 (controls, fxml), `sqlite-jdbc` 3.47.2.0, JUnit 5.11.4 (Jupiter), compiler/surefire/javafx-maven-plugin.
- `module-info.java` + package skeleton under `com.movieticketbooking` (config, controller, model, repository, service, validation, util, view).
- `Main.java`: minimal JavaFX entry point that opens a window.
- Smoke test `MainTest`.
- Full docs tree (see repository README and `phases.md`).

## Verification (performed)

- `mvn test` → BUILD SUCCESS, 1 test, 0 failures.
- `mvn javafx:run` → window opened (`APPLICATION_STARTED`).
- `mvn package` → BUILD SUCCESS.

## Declared only (not implemented — later phases)

Domain model, SQLite schema/seed/repositories, authentication, all feature screens.

## Git

Commit: `phase-01: initialize JavaFX Maven project and project documentation`

## Completion

Phase 01 complete. **STOP.** Do not start Phase 02.