package com.TreasureHunter.CharacterService.service.implement;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.CreateCharacterRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.request.character.SaveCharacterStateRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterRuntimeResponseDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;
import com.TreasureHunter.CharacterService.postgres.CharacterRepository;
import com.TreasureHunter.CharacterService.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

    @Override
    public void createCharacter(CreateCharacterRequestDTO request, Long userId) {
        characterRepository.insertCharacter(request.getCharacterName(), userId);
    }

    @Override
    public DetailCharacterResponseDTO getCharacterProfile(Long userId) {
        log.info("Getting character profile for userId: {}", userId);
        return characterRepository.getCharacterProfile(userId);
    }

    @Override
    public CharacterRuntimeResponseDTO getCharacterState(Long userId) {
        log.info("Getting character state for userId: {}", userId);
        return characterRepository.getCharacterState(userId);
    }

    @Override
    @Transactional
    public void saveCharacterState(UUID characterId, SaveCharacterStateRequestDTO request) {
        if (characterId == null || request == null || request.getPosX() == null || request.getPosY() == null) {
            throw new IllegalArgumentException("Character state requires characterId, posX, and posY");
        }

        log.info("Saving character state for characterId: {}", characterId);
        characterRepository.upsertCharacterState(characterId, request.getPosX(), request.getPosY());
    }
}
