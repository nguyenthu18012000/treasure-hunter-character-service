package com.TreasureHunter.CharacterService.postgres;

import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterRuntimeResponseDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterStateInfoDTO;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.DetailCharacterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.jooq.generated.Tables.CHARACTER;
import static com.example.jooq.generated.Tables.CHARACTER_STATE;

@Service
@RequiredArgsConstructor
public class CharacterRepository {
    private final DSLContext dsl;

    /** @return id của nhân vật vừa tạo */
    public UUID insertCharacter(String characterName, Long userId) {
        return dsl.insertInto(CHARACTER)
                .set(CHARACTER.NAME, characterName)
                .set(CHARACTER.USER_ID, userId)
                .set(CHARACTER.CREATED_AT, LocalDateTime.now())
                .returning(CHARACTER.ID)
                .fetchOne(CHARACTER.ID);
    }

    /**
     * @param forUpdate true: khoá dòng nhân vật tới hết transaction (SELECT ... FOR UPDATE).
     *                  Dùng khi cộng điểm: hai request cùng lúc phải chạy lần lượt, nếu không
     *                  cả hai cùng thấy còn điểm và cộng vượt số điểm có.
     */
    public Optional<CharacterLevel> findCharacterLevel(Long userId, boolean forUpdate) {
        var query = dsl.select(CHARACTER.ID, CHARACTER.LEVEL)
                .from(CHARACTER)
                .where(CHARACTER.USER_ID.eq(userId));

        var record = forUpdate ? query.forUpdate().fetchOne() : query.fetchOne();
        return Optional.ofNullable(record)
                .map(r -> new CharacterLevel(r.get(CHARACTER.ID), r.get(CHARACTER.LEVEL)));
    }

    public record CharacterLevel(UUID characterId, int level) {
    }

    public DetailCharacterResponseDTO getCharacterProfile(Long userId) {
        return dsl.selectFrom(CHARACTER)
                .where(CHARACTER.USER_ID.eq(userId))
                .fetchOneInto(DetailCharacterResponseDTO.class);
    }

    public CharacterRuntimeResponseDTO getCharacterState(Long userId) {
        return findCharacterState(CHARACTER.USER_ID.eq(userId));
    }

    public CharacterRuntimeResponseDTO getCharacterStateById(UUID characterId) {
        return findCharacterState(CHARACTER.ID.eq(characterId));
    }

    /** Thông tin và vị trí nhân vật. Chỉ số (stats) không nằm trong DB mà do CharacterStatService tính. */
    private CharacterRuntimeResponseDTO findCharacterState(Condition condition) {
        Record record = dsl.select(
                        CHARACTER.ID,
                        CHARACTER.NAME,
                        CHARACTER_STATE.POS_X,
                        CHARACTER_STATE.POS_Y
                )
                .from(CHARACTER)
                .leftJoin(CHARACTER_STATE)
                .on(CHARACTER_STATE.CHARACTER_ID.eq(CHARACTER.ID))
                .where(condition)
                .fetchOne();

        if (record == null) {
            return null;
        }

        CharacterStateInfoDTO state = new CharacterStateInfoDTO();
        state.setPosX(record.get(CHARACTER_STATE.POS_X));
        state.setPosY(record.get(CHARACTER_STATE.POS_Y));

        CharacterRuntimeResponseDTO response = new CharacterRuntimeResponseDTO();
        response.setCharacterId(record.get(CHARACTER.ID));
        response.setName(record.get(CHARACTER.NAME));
        response.setState(state);

        return response;
    }

    public void upsertCharacterState(UUID characterId, Double posX, Double posY) {
        dsl.insertInto(CHARACTER_STATE)
                .set(CHARACTER_STATE.CHARACTER_ID, characterId)
                .set(CHARACTER_STATE.POS_X, posX)
                .set(CHARACTER_STATE.POS_Y, posY)
                .onConflict(CHARACTER_STATE.CHARACTER_ID)
                .doUpdate()
                .set(CHARACTER_STATE.POS_X, posX)
                .set(CHARACTER_STATE.POS_Y, posY)
                .execute();

        dsl.update(CHARACTER)
                .set(CHARACTER.UPDATED_AT, LocalDateTime.now())
                .where(CHARACTER.ID.eq(characterId))
                .execute();
    }
}
