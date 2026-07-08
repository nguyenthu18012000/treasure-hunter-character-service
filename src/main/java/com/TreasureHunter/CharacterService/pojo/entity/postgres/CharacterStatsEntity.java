package com.TreasureHunter.CharacterService.pojo.entity.postgres;

import lombok.Data;

import java.util.UUID;

@Data
public class CharacterStatsEntity {
    private UUID characterId;

    private Double speed;
}

