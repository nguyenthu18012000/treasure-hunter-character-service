package com.TreasureHunter.CharacterService.controller;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.CreateCharacterRequestDTO;
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

    @GetMapping("/detail")
    public ResponseEntity<BaseResponse<DetailCharacterResponseDTO>> getCharacterDetail(
            @RequestHeader("X-User-Id") Long userId
    ) {
        DetailCharacterResponseDTO detailCharacter = characterService.getCharacterDetail(userId);
        BaseResponse<DetailCharacterResponseDTO> response = new BaseResponse<>(
                CommonConstant.RESPONSE_CODE.SUCCESS,
                CommonConstant.RESPONSE_MESSAGE.SUCCESS,
                detailCharacter
        );
        return ResponseEntity.ok(response);
    }
}
