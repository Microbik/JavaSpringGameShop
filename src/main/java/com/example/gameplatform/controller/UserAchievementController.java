package com.example.gameplatform.controller;

import com.example.gameplatform.dto.AchievementAssignRequest;
import com.example.gameplatform.service.UserAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-achievements")
public class UserAchievementController {

    private final UserAchievementService userAchievementService;

    @PreAuthorize("hasAnyRole('GAMEMANAGER', 'ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<String> assignAchievement(@RequestBody AchievementAssignRequest request) {
        boolean success = userAchievementService.assignAchievement(
                request.getUserId(), request.getAchievementId());
        if (success) {
            return ResponseEntity.ok("Achievement assigned.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Achievement already assigned.");
        }
    }
}

