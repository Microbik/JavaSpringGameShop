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
        this.achievementService = achievementService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        return ResponseEntity.ok(achievementService.getAllAchievements());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable Long id) {
        return ResponseEntity.ok(achievementService.getAchievementById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
        return ResponseEntity.ok(achievementService.createAchievement(achievement));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    public ResponseEntity<Achievement> updateAchievement(
            @PathVariable Long id,
            @RequestBody Achievement achievementDetails
    ) {
        return ResponseEntity.ok(achievementService.updateAchievement(id, achievementDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}
