package com.TreasureHunter.CharacterService.pojo.dto.response.character;

import lombok.Data;

import java.util.UUID;

@Data
public class CharacterRuntimeResponseDTO {
    private UUID characterId;

    private String name;

    private CharacterStateInfoDTO state;

    private CharacterStatsInfoDTO stats;
}
