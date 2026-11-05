package com.TreasureHunter.CharacterService.config;

import com.TreasureHunter.CommonLib.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Topic DLT chứa các message character-service xử lý lỗi.
 * Service nhận là bên tạo DLT; topic chính do service gửi (socket-service) tạo.
 */
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic characterPositionDeadLetterTopic() {
        return TopicBuilder.name(KafkaTopics.CHARACTER_POSITION_UPDATED + KafkaTopics.DLT_SUFFIX)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
