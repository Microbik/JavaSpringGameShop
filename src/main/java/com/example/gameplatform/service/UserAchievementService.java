package com.example.gameplatform.service;

import com.example.gameplatform.model.Achievement;
import com.example.gameplatform.model.Game;
import com.example.gameplatform.model.User;
import com.example.gameplatform.model.UserAchievement;
import com.example.gameplatform.repository.AchievementRepository;
import com.example.gameplatform.repository.UserAchievementRepository;
import com.example.gameplatform.repository.UserGameRepository;
import com.example.gameplatform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAchievementService {

    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserGameRepository userGameRepository;

    @Transactional
    public boolean assignAchievement(Long userId, Long achievementId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found"));

        Game game = achievement.getGame();

        if (!userGameRepository.existsByUserIdAndGameId(userId, game.getId())) {
            throw new IllegalStateException("User does not own the game");
        }

        if (userAchievementRepository.existsByUserAndAchievement(user, achievement)) {
            return false;
        }

        userAchievementRepository.save(new UserAchievement(user, achievement));

        // Начисляем опыт пользователю
        int currentExp = user.getExperiencePoints();
        int addExp = achievement.getExperiencePoints() != null ? achievement.getExperiencePoints() : 0;
        user.setExperiencePoints(currentExp + addExp);

        userRepository.save(user);

        return true;
    }

}


