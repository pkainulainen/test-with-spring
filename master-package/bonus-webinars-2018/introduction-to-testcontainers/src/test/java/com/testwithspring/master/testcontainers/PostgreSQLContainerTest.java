package com.testwithspring.master.testcontainers;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class demonstrates how you can run a PostgreSQL database
 * with TestContainers.
 */
public class PostgreSQLContainerTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQL = new PostgreSQLContainer();

    @Test
    public void shouldRunPostgreSQLContainer() throws SQLException {
        Connection conn = DriverManager.getConnection(
                postgreSQL.getJdbcUrl(),
                postgreSQL.getUsername(),
                postgreSQL.getPassword()
        );

        ResultSet resultSet = conn.createStatement().executeQuery("SELECT 42");
        resultSet.next();

        int result = resultSet.getInt(1);
        assertThat(result).isEqualByComparingTo(42);
    }
}
