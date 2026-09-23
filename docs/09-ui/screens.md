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

## Home (Phase 05)

- **Controls:** welcome `Label`, "Browse Movies" button.
- **Navigation:** → Movie listing.

## Movie listing (Phase 05)

- **Purpose:** Browse available movies with genre and price.
- **Controls:** movie `ListView` (title | genre | $price | duration), Select Movie, Back.
- **Validation:** none (empty state handled: "No movies available.").
- **Navigation:** Select → Show selection; Back → Home.
- **Controller:** `MovieListController`.
- **Service calls:** `MovieService.listMovies`.
- **Data loaded:** movies from SQLite.

## Seat selection (Phase 06)

- **Purpose:** Pick available seats on a row/column grid.
- **Controls:** seat `ToggleButton` grid, go legend (Available/Selected/Booked), Continue, Back.
- **Validation:** booked seats are disabled; Continue enabled only when ≥ 1 seat selected.
- **Navigation:** Continue → Customer details (Phase 07); Back → Show selection.
- **Controller:** `SeatController`.
- **Service calls:** `SeatService.seatsForShow`.
- **Data loaded:** show's seat grid incl. booked state.

## Customer details (Phase 07)

- **Purpose:** Enter the booker's details.
- **Controls:** full name, email, phone (optional) `TextField`s, Review Booking, Back to Seats.
- **Validation:** non-blank name, valid email (from `Person`); errors inline.
- **Navigation:** Review → Booking summary; Back → Seat selection.
- **Controller:** `CustomerController`.

## Booking summary (Phase 07)

- **Purpose:** Review and confirm the booking before persistence.
- **Controls:** customer, movie/genre, theatre, show time, seats, unit price, tickets, total; Confirm Booking, Back.
- **Validation:** none beyond prior steps.
- **Navigation:** Confirm → Booking recorded (booking ID; full confirmation Phase 08); Back → Customer details.
- **Controller:** `BookingSummaryController`.
- **Service calls:** `BookingService.confirmBooking` (on confirm).
- **Data loaded:** show, seats, customer, `Ticket` total.

## Show selection (Phase 05)

- **Purpose:** Pick a show timing for the selected movie.
- **Controls:** show `ListView` (theatre + location | start time), Select Show, Back to Movies.
- **Validation:** none (empty state handled).
- **Navigation:** Select → Seat selection (Phase 06); Back → Movie listing.
- **Controller:** `ShowController`.
- **Service calls:** `ShowService.showsForMovie`.
- **Data loaded:** shows (with movie + theatre) filtered by movie.