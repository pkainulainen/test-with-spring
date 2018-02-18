package com.testwithspring.master;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import({ExampleApplication.class})
public class IntegrationTestContext {

    @Bean
    DatabaseConfigBean databaseConfigBean() {
        DatabaseConfigBean config = new DatabaseConfigBean();
        config.setAllowEmptyFields(true);
        return config;
    }

    @Bean
    DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource,
                                                                     DatabaseConfigBean databaseConfigBean) {
        DatabaseDataSourceConnectionFactoryBean cf =
                new DatabaseDataSourceConnectionFactoryBean();
        cf.setDataSource(dataSource);
        cf.setDatabaseConfig(databaseConfigBean);
        return cf;
    }
}
