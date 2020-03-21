package com.testwithspring.master

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


/**
 * This class resets the auto incremented id columns. In other words,
 * this class helps you to ensure that the id that is assigned
 * to a new database row is always 1 as long as you reset
 * the database sequences in the setup method.
 *
 * Note that this works ONLY with PostgreSQL when you use
 * auto incremented id columns.
 */
class IdColumnReset internal constructor(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    companion object {
        private const val QUERY_RESET_ID_COLUMN_TEMPLATE = "ALTER SEQUENCE %s RESTART WITH 1"
    }

    fun resetIdColumns(vararg tableNames: String) {
        tableNames.forEach {
            val query = String.format(QUERY_RESET_ID_COLUMN_TEMPLATE, it + "_id_seq")
            jdbcTemplate.update(query, emptyMap<String, String>())
        }
    }
}