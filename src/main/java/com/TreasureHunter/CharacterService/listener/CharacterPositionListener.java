package com.TreasureHunter.CharacterService.listener;

import com.TreasureHunter.CharacterService.pojo.dto.request.character.SaveCharacterStateRequestDTO;
import com.TreasureHunter.CharacterService.service.CharacterService;
import com.TreasureHunter.CommonLib.kafka.KafkaTopics;
import com.TreasureHunter.CommonLib.kafka.event.character.CharacterPositionChangedEvent;
import com.TreasureHunter.CommonLib.kafka.exception.NonRetryableEventException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Nhận vị trí nhân vật do socket-service gửi qua Kafka rồi lưu vào DB.
 * Đóng vai trò như một controller: chỉ kiểm tra dữ liệu và gọi service, logic lưu nằm ở CharacterService.
 *
 * Lỗi khi xử lý (ví dụ DB lỗi) sẽ được retry rồi chuyển sang topic DLT (cấu hình trong common-lib).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CharacterPositionListener {

    private final CharacterService characterService;

    @KafkaListener(topics = KafkaTopics.CHARACTER_POSITION_UPDATED)
    public void handle(CharacterPositionChangedEvent event) {
        if (event.getCharacterId() == null || !isFinite(event.getPosX()) || !isFinite(event.getPosY())) {
            // Dữ liệu sai thì retry cũng không đúng được, chuyển thẳng sang DLT.
            throw new NonRetryableEventException("Invalid position event " + event.getEventId());
        }

        log.debug("Saving position of character {} ({}): ({}, {})",
                event.getCharacterId(), event.getReason(), event.getPosX(), event.getPosY());

        SaveCharacterStateRequestDTO request = new SaveCharacterStateRequestDTO();
        request.setPosX(event.getPosX());
        request.setPosY(event.getPosY());
        characterService.saveCharacterState(event.getCharacterId(), request);
    }

    private static boolean isFinite(Double value) {
        return value != null && Double.isFinite(value);
    }
}
