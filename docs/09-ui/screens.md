# Screens

Per-screen documentation is added as each screen is implemented. Template fields per screen: purpose, controls, inputs, validation, navigation destination, controller, service calls, data loaded.

## Login (Phase 04)

- **Purpose:** Authenticate the local user before showing any features.
- **Controls:** username `TextField`, password `PasswordField`, Login button (default), error `Label`.
- **Inputs:** username, password.
- **Validation:** blank fields / wrong credentials → "Invalid username or password." (handled by `AuthenticationService` + `LoginGate`).
- **Navigation:** success → Home; on failure stays on Login.
- **Controller:** `LoginController`.
- **Service calls:** `AuthenticationService.authenticate`.
- **Data loaded:** none.

## Home (Phase 04, minimal)

- **Purpose:** Greet and navigate.
- **Controls:** welcome `Label` (+ nav in Phase 05).
- **Validation:** none.
- **Navigation:** (Phase 05) → Movie listing.
- **Controller:** `HomeController`.
- **Data loaded:** `User` (from login).