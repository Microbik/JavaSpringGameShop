package com.example.gameplatform.repository;

import com.example.gameplatform.model.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {

    @Query("SELECT ug FROM UserGenre ug WHERE ug.user.id = :userId AND ug.genre.id = :genreId")
    Optional<UserGenre> findByUserIdAndGenreId(@Param("userId") Long userId, @Param("genreId") Long genreId);
}