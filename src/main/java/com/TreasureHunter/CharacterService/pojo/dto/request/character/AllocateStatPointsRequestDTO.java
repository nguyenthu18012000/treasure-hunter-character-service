package com.TreasureHunter.CharacterService.pojo.dto.request.character;

import lombok.Data;

import java.util.Map;

/**
 * Cộng điểm vào nhiều chỉ số trong một lần, ví dụ:
 * <pre>{"points": {"max_hp": 2, "atk": 1}}</pre>
 */
@Data
public class AllocateStatPointsRequestDTO {
    /** stat_key -> số điểm muốn cộng thêm (phải > 0) */
    private Map<String, Integer> points;
}
