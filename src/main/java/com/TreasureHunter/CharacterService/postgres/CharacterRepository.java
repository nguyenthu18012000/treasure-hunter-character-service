package com.TreasureHunter.CharacterService.postgres;

import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterRuntimeResponseDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterStateInfoDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterStatsInfoDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record5;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.jooq.generated.Tables.CHARACTER;
import static com.example.jooq.generated.Tables.CHARACTER_STATE;
import static com.example.jooq.generated.Tables.CHARACTER_STATS;

@Service
@RequiredArgsConstructor
public class CharacterRepository {
    private final DSLContext dsl;

    public void insertCharacter(String characterName, Long userId) {
        dsl.insertInto(CHARACTER)
                .set(CHARACTER.NAME, characterName)
                .set(CHARACTER.USER_ID, userId)
                .set(CHARACTER.CREATED_AT, LocalDateTime.now())
                .returning()
                .fetchOne();
    }

    public DetailCharacterResponseDTO getCharacterProfile(Long userId) {
        return dsl.selectFrom(CHARACTER)
                .where(CHARACTER.USER_ID.eq(userId))
                .fetchOneInto(DetailCharacterResponseDTO.class);
    }

    public CharacterRuntimeResponseDTO getCharacterState(Long userId) {
        Record5<UUID, String, Double, Double, Double> record = dsl.select(
                        CHARACTER.ID,
                        CHARACTER.NAME,
                        CHARACTER_STATE.POS_X,
                        CHARACTER_STATE.POS_Y,
                        CHARACTER_STATS.SPEED
                )
                .from(CHARACTER)
                .leftJoin(CHARACTER_STATE)
                .on(CHARACTER_STATE.CHARACTER_ID.eq(CHARACTER.ID))
                .leftJoin(CHARACTER_STATS)
                .on(CHARACTER_STATS.CHARACTER_ID.eq(CHARACTER.ID))
                .where(CHARACTER.USER_ID.eq(userId))
                .fetchOne();

        if (record == null) {
            return null;
        }

        CharacterStateInfoDTO state = new CharacterStateInfoDTO();
        state.setPosX(record.value3());
        state.setPosY(record.value4());

        CharacterStatsInfoDTO stats = new CharacterStatsInfoDTO();
        stats.setSpeed(record.value5());

        CharacterRuntimeResponseDTO response = new CharacterRuntimeResponseDTO();
        response.setCharacterId(record.value1());
        response.setName(record.value2());
        response.setState(state);
        response.setStats(stats);

        return response;
    }
}
