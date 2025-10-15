package com.TreasureHunter.CharacterService.pojo.entity.postgres;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CharacterEntity {
    private UUID id;

    private Long userId;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
