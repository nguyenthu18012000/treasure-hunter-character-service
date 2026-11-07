package com.TreasureHunter.CharacterService.stat;

/** Một dòng của bảng stat_config. maxPoints = null là không giới hạn điểm. */
public record StatDefinition(
        String key,
        String name,
        double baseValue,
        double perPoint,
        boolean allocatable,
        Integer maxPoints
) {
    /**
     * Số điểm thực sự được tính. Nếu config hạ max_points xuống thấp hơn số điểm nhân vật đã cộng,
     * phần dư không có tác dụng (vẫn còn trong DB, trả lại được khi có tính năng reset điểm).
     */
    public int effectivePoints(int points) {
        int nonNegative = Math.max(points, 0);
        return maxPoints == null ? nonNegative : Math.min(nonNegative, maxPoints);
    }

    public double valueAt(int points) {
        return baseValue + effectivePoints(points) * perPoint;
    }
}
