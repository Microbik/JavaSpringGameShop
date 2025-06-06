package com.example.gameplatform.service;

import com.example.gameplatform.model.Achievement;
import com.example.gameplatform.repository.AchievementRepository;
import com.example.gameplatform.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;

    @Autowired
    public AchievementServiceImpl(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }

    @Override
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    @Override
    public Achievement getAchievementById(Long id) {
        return achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + id));
    }

    @Override
    public Achievement createAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    @Override
    public Achievement updateAchievement(Long id, Achievement achievementDetails) {
        Achievement achievement = getAchievementById(id);
        achievement.setName(achievementDetails.getName());
        achievement.setExperiencePoints(achievementDetails.getExperiencePoints());
        achievement.setGame(achievementDetails.getGame());
        return achievementRepository.save(achievement);
    }

    @Override
    public void deleteAchievement(Long id) {
        achievementRepository.deleteById(id);
    }
}