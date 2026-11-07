package com.TreasureHunter.CharacterService.pojo.dto.response.character;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/** Màn hình chỉ số: đủ thông tin để UI hiển thị và cho người chơi cộng điểm. */
@Data
public class CharacterStatsResponseDTO {
    private UUID characterId;
    private int level;
    /** Tổng điểm nhận được theo level */
    private int totalPoints;
    private int spentPoints;
    private int availablePoints;
    /** Theo thứ tự sort_order của stat_config */
    private List<StatEntry> stats;

    @Data
    public static class StatEntry {
        private String key;
        private String name;
        /** Giá trị cuối đã tính */
        private double value;
        /** Số điểm đã cộng vào chỉ số này */
        private int points;
        /** Mỗi điểm cộng thêm bao nhiêu */
        private double perPoint;
        private boolean allocatable;
        /** null = không giới hạn */
        private Integer maxPoints;
    }
}
