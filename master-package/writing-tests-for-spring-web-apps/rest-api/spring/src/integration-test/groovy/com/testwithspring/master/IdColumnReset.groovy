package com.testwithspring.master

class IdColumnReset {

    private static final QUERY_RESET_ID_COLUMN_TEMPLATE = 'ALTER TABLE %s ALTER COLUMN id RESTART WITH 1'

    private final jdbcTemplate

    IdColumnReset(jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate
    }

    def resetIdColumns(String... tableNames) {
        tableNames.each {t ->
            def query = String.format(QUERY_RESET_ID_COLUMN_TEMPLATE, t)
            jdbcTemplate.update(query, new HashMap<>())
        }
    }
}
