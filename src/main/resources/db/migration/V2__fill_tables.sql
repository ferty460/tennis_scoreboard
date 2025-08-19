INSERT INTO player (name)
VALUES ('Roger Federer'),
       ('Rafael Nadal'),
       ('Novak Djokovic'),
       ('Serena Williams'),
       ('Ashleigh Barty'),
       ('Fedorino Balerino'),
       ('Daniil Medvedev'),
       ('Carlos Alcaraz'),
       ('Iga Świątek'),
       ('Stefanos Tsitsipas'),
       ('Naomi Osaka');

INSERT INTO match (first_player_id, second_player_id, winner_id)
VALUES (1, 2, 1),
       (3, 7, 7),
       (2, 5, 2),
       (10, 1, 10),
       (3, 6, 6),
       (8, 7, 8),
       (1, 4, 4);
