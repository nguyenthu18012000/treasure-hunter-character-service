package com.TreasureHunter.CharacterService.controller;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.CreateCharacterRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterRuntimeResponseDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;
import com.TreasureHunter.CharacterService.service.CharacterService;
import com.TreasureHunter.CommonLib.constant.CommonConstant;
import com.TreasureHunter.CommonLib.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/character")
@RequiredArgsConstructor
public class CharacterController {
    private final CharacterService characterService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<Void>> createCharacter(
            @RequestBody CreateCharacterRequestDTO request,
            @RequestHeader("X-User-Id") Long userId
    ) {
        characterService.createCharacter(request, userId);
        BaseResponse<Void> response = new BaseResponse<>(
                CommonConstant.RESPONSE_CODE.SUCCESS,
                CommonConstant.RESPONSE_MESSAGE.SUCCESS,
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/profile"})
    public ResponseEntity<BaseResponse<DetailCharacterResponseDTO>> getCharacterProfile(
            @RequestHeader("X-User-Id") Long userId
    ) {
        DetailCharacterResponseDTO characterProfile = characterService.getCharacterProfile(userId);
        BaseResponse<DetailCharacterResponseDTO> response = new BaseResponse<>(
                CommonConstant.RESPONSE_CODE.SUCCESS,
                CommonConstant.RESPONSE_MESSAGE.SUCCESS,
                characterProfile
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/state"})
    public ResponseEntity<BaseResponse<CharacterRuntimeResponseDTO>> getCharacterState(
            @RequestHeader("X-User-Id") Long userId
    ) {
        CharacterRuntimeResponseDTO characterState = characterService.getCharacterState(userId);
        BaseResponse<CharacterRuntimeResponseDTO> response = new BaseResponse<>(
                CommonConstant.RESPONSE_CODE.SUCCESS,
                CommonConstant.RESPONSE_MESSAGE.SUCCESS,
                characterState
        );
        return ResponseEntity.ok(response);
    }
}
