package com.testwithspring.master

import com.github.springtestdbunit.bean.DatabaseConfigBean
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean
import com.testwithspring.master.config.ExampleApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

import javax.sql.DataSource

@Configuration
@Import(ExampleApplicationContext.class)
class IntegrationTestContext {

    @Bean
    def databaseConfigBean() {
        DatabaseConfigBean config = new DatabaseConfigBean()
        config.setAllowEmptyFields(true)
        return config
    }

    @Bean
    def dbUnitDatabaseConnection(DataSource dataSource,
                                 DatabaseConfigBean databaseConfigBean) {
        DatabaseDataSourceConnectionFactoryBean cf =
                new DatabaseDataSourceConnectionFactoryBean()
        cf.setDataSource(dataSource)
        cf.setDatabaseConfig(databaseConfigBean)
        return cf
    }

    @Bean
    def idColumnReset(NamedParameterJdbcTemplate jdbcTemplate) {
        return new IdColumnReset(jdbcTemplate)
    }
}
