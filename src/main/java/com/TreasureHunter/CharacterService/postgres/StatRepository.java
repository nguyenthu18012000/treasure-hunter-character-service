package com.TreasureHunter.CharacterService.postgres;

import com.example.jooq.generated.tables.records.StatConfigRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.jooq.generated.Tables.CHARACTER_STAT_POINT;
import static com.example.jooq.generated.Tables.STAT_CONFIG;

@Service
@RequiredArgsConstructor
public class StatRepository {
    private final DSLContext dsl;

    public List<StatConfigRecord> findAllStatConfigs() {
        return dsl.selectFrom(STAT_CONFIG)
                .orderBy(STAT_CONFIG.SORT_ORDER, STAT_CONFIG.STAT_KEY)
                .fetch();
    }

    /** stat_key -> số điểm nhân vật đã cộng. Chỉ số chưa cộng điểm thì không có trong map. */
    public Map<String, Integer> findStatPoints(UUID characterId) {
        return dsl.select(CHARACTER_STAT_POINT.STAT_KEY, CHARACTER_STAT_POINT.POINTS)
                .from(CHARACTER_STAT_POINT)
                .where(CHARACTER_STAT_POINT.CHARACTER_ID.eq(characterId))
                .fetchMap(CHARACTER_STAT_POINT.STAT_KEY, CHARACTER_STAT_POINT.POINTS);
    }

    /** Cộng dồn: chưa có dòng thì tạo, có rồi thì points = points cũ + phần cộng thêm. */
    public void addStatPoints(UUID characterId, Map<String, Integer> pointsToAdd) {
        var queries = pointsToAdd.entrySet().stream()
                .map(entry -> dsl.insertInto(CHARACTER_STAT_POINT)
                        .set(CHARACTER_STAT_POINT.CHARACTER_ID, characterId)
                        .set(CHARACTER_STAT_POINT.STAT_KEY, entry.getKey())
                        .set(CHARACTER_STAT_POINT.POINTS, entry.getValue())
                        .onConflict(CHARACTER_STAT_POINT.CHARACTER_ID, CHARACTER_STAT_POINT.STAT_KEY)
                        .doUpdate()
                        .set(CHARACTER_STAT_POINT.POINTS, CHARACTER_STAT_POINT.POINTS.plus(entry.getValue())))
                .toList();
        dsl.batch(queries).execute();
    }
}
