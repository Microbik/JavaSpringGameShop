package com.example.gameplatform.repository;

import com.example.gameplatform.model.Achievement;
import com.example.gameplatform.model.User;
import com.example.gameplatform.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    boolean existsByUserAndAchievement(User user, Achievement achievement);
}
