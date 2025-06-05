CREATE UNIQUE INDEX idx_users_email ON users(email);

CREATE INDEX idx_usergames_user_game ON user_games(user_id, game_id);

CREATE INDEX idx_achievements_highxp ON achievements(experiencepoints)
    WHERE experiencepoints > 50;

CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_games_name_gin ON games USING GIN (name gin_trgm_ops);

CREATE INDEX idx_usergames_brin_date ON user_games
    USING brin(purchase_date);

CREATE INDEX idx_users_hash_email ON users USING hash(email);



