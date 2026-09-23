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

## Planned (later phases)

Services: AuthService, MovieService, ShowService, SeatService, BookingService. Controllers per screen. Views per screen.

Authority: [architecture.md](architecture.md).
