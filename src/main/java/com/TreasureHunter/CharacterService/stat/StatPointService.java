package com.TreasureHunter.CharacterService.stat;

import com.TreasureHunter.CharacterService.constant.CharacterErrorCode;
import com.TreasureHunter.CharacterService.pojo.dto.response.character.CharacterStatsResponseDTO;
import com.TreasureHunter.CharacterService.postgres.CharacterRepository;
import com.TreasureHunter.CharacterService.postgres.CharacterRepository.CharacterLevel;
import com.TreasureHunter.CharacterService.postgres.StatRepository;
import com.TreasureHunter.CommonLib.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Xem chỉ số và cộng điểm cho nhân vật của user đang đăng nhập. */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatPointService {

    private final CharacterRepository characterRepository;
    private final StatRepository statRepository;
    private final StatConfigCache statConfigCache;
    private final CharacterStatService characterStatService;
    private final StatPointProperties statPointProperties;

    public CharacterStatsResponseDTO getStats(Long userId) {
        CharacterLevel character = findCharacter(userId, false);
        return buildResponse(character, statRepository.findStatPoints(character.characterId()));
    }

    /**
     * Cộng điểm vào một hoặc nhiều chỉ số. Hoặc cộng được hết, hoặc không cộng gì (một transaction).
     *
     * @param pointsToAdd stat_key -> số điểm cộng thêm
     */
    @Transactional
    public CharacterStatsResponseDTO allocate(Long userId, Map<String, Integer> pointsToAdd) {
        if (pointsToAdd == null || pointsToAdd.isEmpty()) {
            throw error(CharacterErrorCode.INVALID_STAT_POINT_REQUEST, "No stat points to allocate");
        }

        // Khoá dòng nhân vật trước khi đọc điểm: request cộng điểm khác của cùng nhân vật phải chờ.
        CharacterLevel character = findCharacter(userId, true);
        Map<String, Integer> currentPoints = statRepository.findStatPoints(character.characterId());

        int requestedTotal = 0;
        for (Map.Entry<String, Integer> entry : pointsToAdd.entrySet()) {
            String statKey = entry.getKey();
            Integer amount = entry.getValue();

            if (amount == null || amount <= 0) {
                throw error(CharacterErrorCode.INVALID_STAT_POINT_REQUEST, "Points for " + statKey + " must be greater than 0");
            }

            StatDefinition definition = findDefinition(statKey)
                    .orElseThrow(() -> error(CharacterErrorCode.STAT_NOT_FOUND, "Stat not found: " + statKey));
            if (!definition.allocatable()) {
                throw error(CharacterErrorCode.STAT_NOT_ALLOCATABLE, "Stat cannot be allocated: " + statKey);
            }

            int afterAllocate = currentPoints.getOrDefault(statKey, 0) + amount;
            if (definition.maxPoints() != null && afterAllocate > definition.maxPoints()) {
                throw error(CharacterErrorCode.STAT_MAX_POINTS_EXCEEDED,
                        "Stat " + statKey + " allows at most " + definition.maxPoints() + " points");
            }

            requestedTotal += amount;
        }

        int available = availablePoints(character, currentPoints);
        if (requestedTotal > available) {
            throw error(CharacterErrorCode.NOT_ENOUGH_STAT_POINTS,
                    "Not enough stat points: requested " + requestedTotal + ", available " + available);
        }

        statRepository.addStatPoints(character.characterId(), pointsToAdd);
        log.info("Character {} allocated stat points {}", character.characterId(), pointsToAdd);

        return buildResponse(character, statRepository.findStatPoints(character.characterId()));
    }

    private CharacterStatsResponseDTO buildResponse(CharacterLevel character, Map<String, Integer> points) {
        Map<String, Double> values = characterStatService.calculateStats(points);

        List<CharacterStatsResponseDTO.StatEntry> entries = new ArrayList<>();
        for (StatDefinition definition : statConfigCache.getDefinitions()) {
            CharacterStatsResponseDTO.StatEntry entry = new CharacterStatsResponseDTO.StatEntry();
            entry.setKey(definition.key());
            entry.setName(definition.name());
            entry.setValue(values.get(definition.key()));
            entry.setPoints(points.getOrDefault(definition.key(), 0));
            entry.setPerPoint(definition.perPoint());
            entry.setAllocatable(definition.allocatable());
            entry.setMaxPoints(definition.maxPoints());
            entries.add(entry);
        }

        int total = statPointProperties.totalPoints(character.level());
        int spent = spentPoints(points);

        CharacterStatsResponseDTO response = new CharacterStatsResponseDTO();
        response.setCharacterId(character.characterId());
        response.setLevel(character.level());
        response.setTotalPoints(total);
        response.setSpentPoints(spent);
        // Có thể âm nếu config giảm điểm mỗi level sau khi người chơi đã cộng; khi đó hiển thị 0.
        response.setAvailablePoints(Math.max(total - spent, 0));
        response.setStats(entries);
        return response;
    }

    private int availablePoints(CharacterLevel character, Map<String, Integer> currentPoints) {
        return statPointProperties.totalPoints(character.level()) - spentPoints(currentPoints);
    }

    /** Tính cả điểm của chỉ số đã bị xoá khỏi config hoặc vượt max_points: đã cộng là đã tiêu. */
    private static int spentPoints(Map<String, Integer> points) {
        return points.values().stream().mapToInt(Integer::intValue).sum();
    }

    private CharacterLevel findCharacter(Long userId, boolean forUpdate) {
        return characterRepository.findCharacterLevel(userId, forUpdate)
                .orElseThrow(() -> error(CharacterErrorCode.CHARACTER_NOT_FOUND, "Character not found"));
    }

    private Optional<StatDefinition> findDefinition(String statKey) {
        return statConfigCache.getDefinitions().stream()
                .filter(d -> d.key().equals(statKey))
                .findFirst();
    }

    private static CommonException error(String code, String message) {
        return new CommonException(code, message);
    }
}
