package com.testwithspring.intermediate.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@Configuration
public class PersistenceContext {

    @Bean
    DataSource dataSource(Environment env) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty("db.url"));
        dataSourceConfig.setUsername(env.getRequiredProperty("db.username"));
        dataSourceConfig.setPassword(env.getRequiredProperty("db.password"));

        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    SpringLiquibase liquibase(DataSource dataSource, Environment env) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setContexts(env.getRequiredProperty("liquibase.contexts"));
        liquibase.setChangeLog("classpath:/db/changelog/db.changelog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
