package com.example.gameplatform.repository;

import com.example.gameplatform.model.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    boolean existsByUserIdAndGameId(Long userId, Long gameId);
}
