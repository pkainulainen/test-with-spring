package com.testwithspring.master.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.sql.ResultSet

/**
 *  Provides a function which queries authentiocated users from the
 *  database.
 */
@Service
open class LoggedInUserService(@Autowired private val jdbcTemplate: NamedParameterJdbcTemplate): UserDetailsService {

    companion object {

        private val LOGGER: Logger = LoggerFactory.getLogger(LoggedInUserService::class.java)

        private const val QUERY_FIND_BY_EMAIL_ADDRESS = "SELECT u.id as id, " +
                "u.email_address AS emailAddress, " +
                "u.is_enabled AS isEnabled, " +
                "u.name AS name, " +
                "u.password AS password, " +
                "u.role AS role " +
                "FROM user_accounts u " +
                "WHERE u.email_address = :emailAddress"

        private fun mapToUserDetailsObject(user: User): LoggedInUser {
            return LoggedInUser(
                    id = user.id,
                    enabled = user.enabled,
                    password = user.password,
                    role = user.role,
                    username = user.emailAddress
            )
        }

        private class UserRowMapper: RowMapper<User> {

            override fun mapRow(resultSet: ResultSet, rowNum: Int): User {
                return User(
                        emailAddress = resultSet.getString("emailAddress"),
                        enabled = resultSet.getBoolean("isEnabled"),
                        id = resultSet.getLong("id"),
                        name = resultSet.getString("name"),
                        password = resultSet.getString("password"),
                        role = UserRole.valueOf(resultSet.getString("role"))
                )
            }
        }
    }

    /**
     * Finds the authenticated user from the database by using the
     * username (aka email address) as a search criteria.
     * @param   username    The username (aka email address) of the authenticated user.
     * @return  The information of the found user.
     * @throws  UsernameNotFoundException   If no user is found with the given username (aka email address).
     */
    override fun loadUserByUsername(username: String): UserDetails {
        LOGGER.info("Find user by username: {}", username)

        try {
            val found = findByUsername(username)
            LOGGER.info("Found user: {}", found)

            return mapToUserDetailsObject(found)
        }
        catch (e: EmptyResultDataAccessException) {
            throw UsernameNotFoundException("No user found with username: $username")
        }
    }

    private fun findByUsername(username: String): User {
        return jdbcTemplate.queryForObject(
                QUERY_FIND_BY_EMAIL_ADDRESS,
                mapOf(Pair("emailAddress", username)),
                UserRowMapper()
        )
    }
}