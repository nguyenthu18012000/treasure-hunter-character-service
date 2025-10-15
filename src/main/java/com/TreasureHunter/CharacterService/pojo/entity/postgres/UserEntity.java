package com.TreasureHunter.CharacterService.pojo.entity.postgres;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private Long id;

    private String username;

    private String password;

    private LocalDateTime createdAt = LocalDateTime.now();
}
