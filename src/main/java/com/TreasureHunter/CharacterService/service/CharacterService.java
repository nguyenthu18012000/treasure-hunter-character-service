package com.TreasureHunter.CharacterService.service;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.CreateCharacterRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;

public interface CharacterService {
    void createCharacter(CreateCharacterRequestDTO request);

    DetailCharacterResponseDTO getCharacterDetail();
}
