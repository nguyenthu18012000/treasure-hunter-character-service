package com.TreasureHunter.CharacterService.service.implement;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.CreateCharacterRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterRuntimeResponseDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;
import com.TreasureHunter.CharacterService.postgres.CharacterRepository;
import com.TreasureHunter.CharacterService.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
