package com.TreasureHunter.CharacterService.stat;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Luật nhận điểm cộng, cấu hình trong application.yml (treasure-hunter.character.stat-points):
 *   tổng điểm = startPoints + (level - 1) * pointsPerLevel
 * Đổi giá trị thì áp dụng cho mọi nhân vật, vì tổng điểm không lưu mà tính theo level.
 */
@ConfigurationProperties("treasure-hunter.character.stat-points")
public record StatPointProperties(Integer startPoints, Integer pointsPerLevel) {

    private static final String PREFIX = "treasure-hunter.character.stat-points.";

    public StatPointProperties {
        if (startPoints == null || startPoints < 0) {
            throw new IllegalArgumentException(PREFIX + "start-points is required and must be >= 0");
        }
        if (pointsPerLevel == null || pointsPerLevel < 0) {
            throw new IllegalArgumentException(PREFIX + "points-per-level is required and must be >= 0");
        }
    }

    public int totalPoints(int level) {
        return startPoints + Math.max(level - 1, 0) * pointsPerLevel;
    }
}
