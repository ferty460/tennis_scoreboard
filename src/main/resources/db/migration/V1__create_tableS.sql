CREATE MEMORY TABLE IF NOT EXISTS player
(
    id   BIGINT PRIMARY KEY,
    name VARCHAR(64)
);

CREATE MEMORY TABLE IF NOT EXISTS match
(
    id               BIGINT PRIMARY KEY,
    first_player_id  BIGINT REFERENCES player (id),
    second_player_id BIGINT REFERENCES player (id),
    winner           BIGINT REFERENCES player (id)
);