package com.TreasureHunter.CharacterService.controller;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.AllocateStatPointsRequestDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterStatsResponseDTO;
import com.TreasureHunter.CharacterService.stat.StatPointService;
import com.TreasureHunter.CommonLib.constant.CommonConstant;
import com.TreasureHunter.CommonLib.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Chỉ số và điểm cộng của nhân vật thuộc user đang đăng nhập (X-User-Id do gateway gắn). */
@RestController
@RequestMapping("/character/stats")
@RequiredArgsConstructor
public class CharacterStatController {

    private final StatPointService statPointService;

    @GetMapping
    public ResponseEntity<BaseResponse<CharacterStatsResponseDTO>> getStats(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ok(statPointService.getStats(userId));
    }

    @PostMapping("/allocate")
    public ResponseEntity<BaseResponse<CharacterStatsResponseDTO>> allocate(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody AllocateStatPointsRequestDTO request
    ) {
        return ok(statPointService.allocate(userId, request.getPoints()));
    }

    private static <T> ResponseEntity<BaseResponse<T>> ok(T data) {
        return ResponseEntity.ok(new BaseResponse<>(
                CommonConstant.RESPONSE_CODE.SUCCESS,
                CommonConstant.RESPONSE_MESSAGE.SUCCESS,
                data
        ));
    }
}
