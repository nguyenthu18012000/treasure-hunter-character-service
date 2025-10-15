package com.TreasureHunter.CharacterService.pojo.dto.response.character;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DetailCharacterResponseDTO {
    private UUID id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
