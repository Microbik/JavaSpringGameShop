package com.example.gameplatform.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementAssignRequest {
    private Long userId;
    private Long achievementId;
}
