package com.TreasureHunter.CharacterService.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class JooqConfig {

    /**
     * TransactionAwareDataSourceProxy: để jOOQ dùng chung connection với transaction của Spring.
     * Thiếu nó thì mỗi câu lệnh jOOQ tự lấy connection riêng và tự commit, @Transactional không có tác dụng.
     */
    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        return DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
    }

}
