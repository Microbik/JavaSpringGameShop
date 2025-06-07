package com.example.gameplatform.repository;

import com.example.gameplatform.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findByIdWithLock(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {
            "genrePreferences.genre",
            "games.game",
            "achievements.achievement",
            "addons.addon"
    })
    @Query("SELECT u FROM User u")
    List<User> findAllWithDetails();
}

