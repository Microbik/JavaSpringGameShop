package com.example.gameplatform.controller;

import com.example.gameplatform.model.Achievement;
import com.example.gameplatform.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService; // Внедряем интерфейс
    }

    // GET Все достижения (доступно GameManager и ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        return ResponseEntity.ok(achievementService.getAllAchievements());
    }

    // GET Достижение по ID (доступно GameManager и ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable Long id) {
        return ResponseEntity.ok(achievementService.getAchievementById(id));
    }

    // POST Создать новое достижение (доступно GameManager и ADMIN)
    @PostMapping
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
        return ResponseEntity.ok(achievementService.createAchievement(achievement));
    }

    // PUT Обновить достижение (доступно GameManager и ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Achievement> updateAchievement(
            @PathVariable Long id,
            @RequestBody Achievement achievementDetails
    ) {
        return ResponseEntity.ok(achievementService.updateAchievement(id, achievementDetails));
    }

    // DELETE Удалить достижение (доступно только ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}
