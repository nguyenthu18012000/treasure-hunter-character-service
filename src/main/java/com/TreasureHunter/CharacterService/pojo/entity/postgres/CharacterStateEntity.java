package com.TreasureHunter.CharacterService.pojo.entity.postgres;

import lombok.Data;

import java.util.UUID;

@Data
public class CharacterStateEntity {
    private UUID characterId;

    private Double posX;

    private Double posY;
}

