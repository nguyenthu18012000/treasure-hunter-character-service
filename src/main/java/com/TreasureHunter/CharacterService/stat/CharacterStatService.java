package com.TreasureHunter.CharacterService.stat;

import com.TreasureHunter.CharacterService.postgres.StatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tính chỉ số cuối của nhân vật:
 *   chỉ số = base_value + min(điểm đã cộng, max_points) * per_point
 * Không lưu kết quả: sửa stat_config là mọi nhân vật nhận giá trị mới.
 * Sau này có trang bị thì cộng thêm phần trang bị ở đây.
 */
@Service
@RequiredArgsConstructor
public class CharacterStatService {

    private final StatConfigCache statConfigCache;
    private final StatRepository statRepository;

    public Map<String, Double> calculateStats(UUID characterId) {
        return calculateStats(statRepository.findStatPoints(characterId));
    }

    /** @param points stat_key -> số điểm đã cộng; chỉ số không có trong map = 0 điểm */
    public Map<String, Double> calculateStats(Map<String, Integer> points) {
        Map<String, Double> stats = new LinkedHashMap<>();
        for (StatDefinition definition : statConfigCache.getDefinitions()) {
            int statPoints = points.getOrDefault(definition.key(), 0);
            stats.put(definition.key(), round(definition.valueAt(statPoints)));
        }
        return stats;
    }

    /** Làm tròn 2 chữ số để tránh lỗi số thực kiểu 0.2 * 3 = 0.6000000000000001. */
    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
