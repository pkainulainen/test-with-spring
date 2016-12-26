package com.testwithspring.intermediate;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.HashMap;

/**
 *  This class resets the auto incremented id columns. In other words,
 *  this class helps you to ensure that the id that is assigned
 *  to a new database row is always 1 as long as you reset
 *  the database sequences in the setup method.
 *
 *  Note that this works ONLY with the H2 database.
 */
public class IdColumnReset {

    private static final String QUERY_RESET_ID_COLUMN_TEMPLATE = "ALTER TABLE %s ALTER COLUMN id RESTART WITH 1";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    IdColumnReset(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void resetIdColumns(String... tableNames) {
        Arrays.asList(tableNames).forEach(t -> {
            String query = String.format(QUERY_RESET_ID_COLUMN_TEMPLATE, t);
            jdbcTemplate.update(query, new HashMap<>());
        });
    }
}

