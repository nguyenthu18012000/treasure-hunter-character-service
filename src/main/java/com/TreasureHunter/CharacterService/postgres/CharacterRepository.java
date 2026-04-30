package com.TreasureHunter.CharacterService.postgres;

import com.TreasureHunter.CharacterService.pojo.entity.postgres.CharacterEntity;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.jooq.generated.Tables.CHARACTER;

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

    public CharacterEntity getCharacterDetail(Long userId) {
        return dsl.selectFrom(CHARACTER)
                .where(CHARACTER.USER_ID.eq(userId))
                .fetchOneInto(CharacterEntity.class);
    }
}
