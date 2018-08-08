package com.testwithspring.master.testcontainers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This example demonstrates how we can run a PostgreSQL container
 * with JUnit 5 by using a custom JUnit 5 extension.
 */
@ExtendWith(TestContainersExtension.class)
@DisplayName("Run a PostgreSQL container")
class PostgreSQLContainerExtensionTest {

    private static PostgreSQLContainer postgreSQL = new PostgreSQLContainer();

    @Test
    @DisplayName("Should run a PostgreSQL container")
    void shouldRunPostgreSQLContainer() throws SQLException {
        Connection conn = DriverManager.getConnection(
                postgreSQL.getJdbcUrl(),
                postgreSQL.getUsername(),
                postgreSQL.getPassword()
        );

        ResultSet resultSet = conn.createStatement()
                .executeQuery("SELECT 42");
        resultSet.next();

        int result = resultSet.getInt(1);
        assertThat(result).isEqualByComparingTo(42);
    }
}
