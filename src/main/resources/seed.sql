INSERT OR IGNORE INTO movies (id, title, genre, ticket_price_cents, duration_minutes, synopsis) VALUES
    (1, 'Inception', 'SCI_FI', 1200, 148, 'A thief who steals corporate secrets through dream-sharing technology.'),
    (2, 'The Dark Knight', 'ACTION', 1100, 152, 'Batman faces the chaotic villain Joker in Gotham City.'),
    (3, 'Spirited Away', 'ANIMATION', 1000, 125, 'A young girl is trapped in a mysterious world of spirits.'),
    (4, 'The Godfather', 'DRAMA', 1300, 175, 'The aging patriarch of an organized crime dynasty transfers control to his son.');

INSERT OR IGNORE INTO theatres (id, name, location) VALUES
    (1, 'CineMax', 'Downtown'),
    (2, 'Grand Cinema', 'Northside');

INSERT OR IGNORE INTO shows (id, movie_id, theatre_id, start_time, end_time) VALUES
    (1, 1, 1, '2026-10-01T18:00', '2026-10-01T20:30'),
    (2, 1, 2, '2026-10-02T20:00', '2026-10-02T22:30'),
    (3, 2, 1, '2026-10-01T15:00', '2026-10-01T17:30'),
    (4, 3, 2, '2026-10-02T11:00', '2026-10-02T13:00');