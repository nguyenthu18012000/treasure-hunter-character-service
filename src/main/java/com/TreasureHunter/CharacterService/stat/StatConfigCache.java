package com.TreasureHunter.CharacterService.stat;

import com.TreasureHunter.CharacterService.postgres.StatRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Giữ bảng stat_config trong bộ nhớ: config ít khi đổi, không cần đọc DB mỗi lần tính chỉ số.
 * Sửa stat_config xong thì restart character-service (hoặc gọi reload()).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StatConfigCache {

    private final StatRepository statRepository;

    /** Thay cả list một lần (volatile) nên luồng khác không bao giờ thấy list đang sửa dở. */
    private volatile List<StatDefinition> definitions = List.of();

    @PostConstruct
    public void reload() {
        List<StatDefinition> loaded = statRepository.findAllStatConfigs().stream()
                .map(r -> new StatDefinition(
                        r.getStatKey(),
                        r.getName(),
                        r.getBaseValue(),
                        r.getPerPoint(),
                        r.getAllocatable(),
                        r.getMaxPoints()))
                .toList();

        if (loaded.isEmpty()) {
            // Không có config thì mọi nhân vật đều 0 máu, 0 tốc độ: dừng luôn để phát hiện sớm.
            throw new IllegalStateException("Table stat_config is empty, run db/2026-11-05_stat_config.sql");
        }

        definitions = loaded;
        log.info("Loaded {} stat configs: {}", loaded.size(), loaded.stream().map(StatDefinition::key).toList());
    }

    /** Theo thứ tự sort_order. */
    public List<StatDefinition> getDefinitions() {
        return definitions;
    }
}
