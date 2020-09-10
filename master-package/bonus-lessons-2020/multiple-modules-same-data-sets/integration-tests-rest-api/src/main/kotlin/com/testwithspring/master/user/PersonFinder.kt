package com.testwithspring.master.user

import com.testwithspring.master.common.NotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*


/**
 * Provides a function which queries person information from
 * the database.
 */
@Component
open class PersonFinder(@Autowired private val jdbcTemplate: NamedParameterJdbcTemplate) {

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PersonFinder::class.java)

        private const val QUERY_FIND_PERSON_INFORMATION = "SELECT u.id as id, " +
                "u.name AS name " +
                "FROM user_accounts u " +
                "WHERE u.id = :id"

        private class PersonInformationRowMapper : RowMapper<PersonDTO> {

            @Throws(SQLException::class)
            override fun mapRow(rs: ResultSet, rowNum: Int): PersonDTO {
                return PersonDTO(
                        name = rs.getString("name"),
                        userId = rs.getLong("id")
                )
            }
        }
    }

    /**
     * Finds the person information information of the specified user.
     * @param   userId  The user id of the user who information will be returned.
     * @return  The "found" person information.
     * @throws  NotFoundException   If no person is found with the given user id.
     */
    @Transactional(readOnly = true)
    open fun findPersonInformationByUserId(userId: Long): PersonDTO {
        LOGGER.info("Finding person information by using the user id: {}", userId)

        try {
            val found: PersonDTO = jdbcTemplate.queryForObject(
                    QUERY_FIND_PERSON_INFORMATION,
                    mapOf(Pair("id", userId)),
                    PersonInformationRowMapper()
            )
            LOGGER.info("Found person information: {}", found)
            return found
        } catch (ex: EmptyResultDataAccessException) {
            LOGGER.error("No person information found with user id: {}", userId)
            throw NotFoundException("No person information found with user id: $userId")
        }
    }
}