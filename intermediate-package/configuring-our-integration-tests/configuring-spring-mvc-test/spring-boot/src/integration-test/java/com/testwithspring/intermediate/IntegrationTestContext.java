package com.testwithspring.intermediate;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import com.testwithspring.intermediate.message.MessageApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import({MessageApplication.class})
public class IntegrationTestContext {

    /**
     * This bean allows us to modify the DbUnit configuration
     * properties.
     */
    @Bean
    DatabaseConfigBean databaseConfigBean() {
        DatabaseConfigBean config = new DatabaseConfigBean();
        config.setAllowEmptyFields(true);
        return config;
    }

    /**
     * This bean provides the required database connections
     * to DbUnit.
     * @param dataSource
     * @param databaseConfigBean
     * @return
     */
    @Bean
    DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(DataSource dataSource,
                                                                     DatabaseConfigBean databaseConfigBean) {
        DatabaseDataSourceConnectionFactoryBean cf = new DatabaseDataSourceConnectionFactoryBean();
        cf.setDataSource(dataSource);
        cf.setDatabaseConfig(databaseConfigBean);
        return cf;
    }
}

