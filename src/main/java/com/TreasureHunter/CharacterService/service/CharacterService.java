package com.TreasureHunter.CharacterService.service;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.CreateCharacterRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterRuntimeResponseDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;

public interface CharacterService {
    void createCharacter(CreateCharacterRequestDTO request, Long userId);

    DetailCharacterResponseDTO getCharacterProfile(Long userId);

    CharacterRuntimeResponseDTO getCharacterState(Long userId);
}
