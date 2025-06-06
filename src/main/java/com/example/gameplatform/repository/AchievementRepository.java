package com.example.gameplatform.repository;

import com.example.gameplatform.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {}