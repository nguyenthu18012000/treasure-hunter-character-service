package com.TreasureHunter.CharacterService.service.implement;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.CreateCharacterRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;
import com.TreasureHunter.CharacterService.postgres.CharacterRepository;
import com.TreasureHunter.CharacterService.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

//    private final AuthService authService;

    @Override
    public void createCharacter(CreateCharacterRequestDTO request) {
//        Long userId = authService.getUserId();
//        characterRepository.insertCharacter(request.getCharacterName(), userId);
    }

    @Override
    public DetailCharacterResponseDTO getCharacterDetail() {
//        Long userId = authService.getUserId();

        Character character = characterRepository.getCharacterDetail(6L);
        if (character == null) {
            return null;
        }

        DetailCharacterResponseDTO response = new DetailCharacterResponseDTO();
        BeanUtils.copyProperties(character, response);

        return response;
    }
}
