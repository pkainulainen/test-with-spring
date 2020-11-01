package com.testwithspring.master.dbunitdatatypes.config;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Ensures that our custom DbUnit data types are used
 * by DbUnit when it initializes our database into a known
 * state before our integration tests are run.
 */
@Configuration
public class TestDatabaseConfiguration {

    @Bean
    DatabaseConfigBean dbUnitDatabaseConfig() {
        DatabaseConfigBean config = new DatabaseConfigBean();
        config.setDatatypeFactory(new CustomPostgreSQLDataTypeFactory());
        return config;
    }

    @Bean
    DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource, DatabaseConfigBean dbConfig) {
        DatabaseDataSourceConnectionFactoryBean cf = new DatabaseDataSourceConnectionFactoryBean();
        cf.setDataSource(dataSource);
        cf.setDatabaseConfig(dbConfig);
        return cf;
    }
}
