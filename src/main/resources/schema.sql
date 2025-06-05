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

INSERT INTO roles (role_id, name) VALUES (1, 'USER') ON CONFLICT DO NOTHING;
INSERT INTO roles (role_id, name) VALUES (2, 'ADMIN') ON CONFLICT DO NOTHING;
