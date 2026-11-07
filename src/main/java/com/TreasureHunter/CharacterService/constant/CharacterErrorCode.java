package com.TreasureHunter.CharacterService.constant;

/** Mã lỗi nghiệp vụ của character-service, trả về trong BaseResponse.code (HTTP 400). */
public final class CharacterErrorCode {
    public static final String CHARACTER_NOT_FOUND = "200";
    public static final String INVALID_STAT_POINT_REQUEST = "201";
    public static final String STAT_NOT_FOUND = "202";
    public static final String STAT_NOT_ALLOCATABLE = "203";
    public static final String STAT_MAX_POINTS_EXCEEDED = "204";
    public static final String NOT_ENOUGH_STAT_POINTS = "205";

    private CharacterErrorCode() {
    }
}
