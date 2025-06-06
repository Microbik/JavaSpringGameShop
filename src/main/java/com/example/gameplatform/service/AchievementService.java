package com.example.gameplatform.service;

import com.example.gameplatform.model.Achievement;
import java.util.List;

public interface AchievementService {
    List<Achievement> getAllAchievements();
    Achievement getAchievementById(Long id);
    Achievement createAchievement(Achievement achievement);
    Achievement updateAchievement(Long id, Achievement achievementDetails);
    void deleteAchievement(Long id);
}