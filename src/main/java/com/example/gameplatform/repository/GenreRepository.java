package com.example.gameplatform.repository;

import com.example.gameplatform.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    boolean existsByName(String name); // Для проверки уникальности имени
}
