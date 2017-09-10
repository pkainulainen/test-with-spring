package com.testwithspring.master

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class IdColumnReset {

    private static final String QUERY_RESET_ID_COLUMN_TEMPLATE = 'ALTER TABLE %s ALTER COLUMN id RESTART WITH 1'

    private final NamedParameterJdbcTemplate jdbcTemplate

    IdColumnReset(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
    }

    def resetIdColumns(String... tableNames) {
        tableNames.each {t ->
            def query = String.format(QUERY_RESET_ID_COLUMN_TEMPLATE, t)
            jdbcTemplate.update(query, new HashMap<>())
        }
    }
}
