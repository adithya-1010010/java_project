# Component Design

## Repository layer (Phase 03)

| Component | Responsibility |
|---|---|
| `config.Database` | Path/URL, `open()` (FK on), `init()` (schema + seed) |
| `config.Seeder` | Idempotent seat grids + demo users |
| `MovieRepository` | Read movies |
| `TheatreRepository` | Read theatres |
| `ShowRepository` | Read shows (with movie + theatre) |
| `SeatRepository` | Read grids, insert layout, mark booked |
| `CustomerRepository` | Insert / read customers |
| `UserRepository` | Insert / read by username (auth, Phase 04) |
| `BookingRepository` | Transactional create, read by code / list |

## Service layer (Phase 04)

| Component | Responsibility |
|---|---|
| `service.AuthenticationService` | Hash + verify credentials against `users` |
| `controller.LoginGate` | Enter app only on successful authentication |
| `controller.LoginController` | Login screen + error handling |
| `controller.HomeController` | Minimal home greeting screen |

## Planned (later phases)

Services: MovieService, ShowService, SeatService, BookingService. Controllers/views for movie, show, seat, customer, summary, confirmation screens.

Authority: [architecture.md](architecture.md).
