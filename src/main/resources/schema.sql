-- Создаем расширение (должно быть первым)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Индексы для users
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_hash_email ON users USING hash(email);

-- Индексы для user_games
CREATE INDEX IF NOT EXISTS idx_usergames_user_game ON user_games(user_id, game_id);
CREATE INDEX IF NOT EXISTS idx_usergames_brin_date ON user_games USING brin(purchase_date);

-- Индексы для achievements
CREATE INDEX IF NOT EXISTS idx_achievements_highxp ON achievements(experiencepoints)
    WHERE experiencepoints > 50;

-- Индекс для games
CREATE INDEX IF NOT EXISTS idx_games_name_gin ON games USING GIN (name gin_trgm_ops);

INSERT INTO roles (role_id, name) VALUES (1, 'Player') ON CONFLICT DO NOTHING;
INSERT INTO roles (role_id, name) VALUES (2, 'ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (role_id, name) VALUES (3, 'GameManager') ON CONFLICT DO NOTHING;

insert into "users" ("name", "email", "password", "role_id", "age", "balance", "experiencepoints") values
                                                                                                       ('Thegre', 'aibaa@example.com', 'J35ih35j2', 1, 25, 100.00, 0),
                                                                                                       ('Mekeken4', 'hex7@example.com', 'hi53hi25oigoih52igho2i', 1, 30, 150.50, 0),
                                                                                                       ('Teteggeo', 'tetr67@example.com', 'g5j2h5892hg985h89h298hg', 1, 16, 70.50, 0),
                                                                                                       ('Blebb', 'blebb7@example.com', '5ijh5ihg5hgh5hg59h659', 1, 15, 40, 0),
                                                                                                       ('aiaiaiaiaa', 'lelelel@example.com', 'ju594jgj5g5g95hg999', 1, 18, 50.75, 0),
                                                                                                       ('Ifgen56', 'Guruga532@gmail.com', 'rogj2gj59h2gh2h05j2nh', 1, 14, 5, 0);

insert into "genres" ("name") values
                                  ('Action'),
                                  ('RPG'),
                                  ('Puzzles'),
                                  ('Strategy'),
                                  ('Simulator'),
                                  ('Tower_defence'),
                                  ('Adventure'),
                                  ('MOBA'),
                                  ('Roguelike');

insert into "games" ("genre_id", "name", "rating", "price", "releasedate", "publisher") values
                                                                                            (2, 'Assassin`s Creed Origin', 16, 59.99, '2018-05-10', 'Ubisoft'),
                                                                                            (9, 'Fallout 4', 18, 49.99, '2015-11-15', 'Bethesda'),
                                                                                            (3, 'Funny puzzles', 12, 19.99, '2021-08-21', 'Alen'),
                                                                                            (4, 'Hearts of Iron 4', 18, 39.99, '2020-02-10', 'Paradox Interactive'),
                                                                                            (5, 'Powerlifter simulator', 14, 4.99, '2023-02-01', 'Robobox'),
                                                                                            (8, 'Deadlock', 16, 0, '2024-03-02', 'Valve'),
                                                                                            (9, 'Risk of Rain', 14, 9.99, '2018-05-04', 'Hopoo');

insert into "addons" ("game_id", "name", "price", "type") values
                                                              (1, 'Max level bundle', 9.99, 'IN_GAME_PURCHASE'),
                                                              (4, 'Gotterdamerrung', 5.99, 'DLC'),
                                                              (1, 'Golden horse', 6.99, 'IN_GAME_PURCHASE'),
                                                              (4, 'Graveyard of empires', 7.99, 'DLC'),
                                                              (7, 'Survivors of the Void', 5.99, 'DLC');

insert into "achievements" ("game_id", "name", "experiencepoints") values
                                                                       (1, 'Last assassin', 50),
                                                                       (4, 'Destroyer of Worlds', 30),
                                                                       (4, 'History repeated itself', 75),
                                                                       (3, 'Pretty collector', 60),
                                                                       (7, 'Full complete', 100),
                                                                       (5, 'Heavy heavy', 55);

insert into "user_games" ("user_id", "game_id", "purchase_date") values
                                                                     (1, 1, '2024-01-05'),
                                                                     (1, 2, '2024-02-12'),
                                                                     (2, 3, '2024-03-20'),
                                                                     (3, 4, '2025-01-01'),
                                                                     (4, 5, '2025-02-03'),
                                                                     (4, 6, '2025-02-03'),
                                                                     (4, 7, '2025-02-03'),
                                                                     (5, 7, '2025-02-08'),
                                                                     (6, 4, '2025-03-01');

insert into "user_achievements" ("user_id", "achievement_id","unlock_date") values
                                                                  (3, 2,'2025-03-01'),
                                                                  (3, 3, '2025-03-02'),
                                                                  (4, 5, '2025-03-03'),
                                                                  (5, 5, '2025-03-06'),
                                                                  (4, 6, '2025-03-09');

insert into "user_addons" ("user_id", "addon_id", "purchase_date") values
                                                                       (3, 2, '2024-03-20'),
                                                                       (4, 5, '2025-02-05'),
                                                                       (1, 1, '2024-09-01'),
                                                                       (6, 2, '2025-01-01');

insert into user_genres ("user_id", "genre_id") values
                                                    (1,2),
                                                    (1,4),
                                                    (2,5),
                                                    (4,8),
                                                    (3,5),
                                                    (1,7),
                                                    (5,2),
                                                    (5,3),
                                                    (6,1),
                                                    (6,3),
                                                    (6,6);
