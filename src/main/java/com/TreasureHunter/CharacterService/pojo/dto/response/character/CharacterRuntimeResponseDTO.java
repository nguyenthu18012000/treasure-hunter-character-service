package com.TreasureHunter.CharacterService.pojo.dto.response.character;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class CharacterRuntimeResponseDTO {
    private UUID characterId;

    private String name;

    private CharacterStateInfoDTO state;

    /** Chỉ số cuối đã tính, theo stat_key (xem StatKey trong common-lib), ví dụ {"max_hp": 120.0, "speed": 160.0} */
    private Map<String, Double> stats;
}
