package com.testwithspring.intermediate.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements CustomUserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private static final String QUERY_FIND_PERSON_INFORMATION = "SELECT u.id as id, " +
            "u.name AS name " +
            "FROM user_accounts u " +
            "WHERE u.id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PersonDTO> findPersonInformationById(Long id) {
        LOGGER.info("Finding person information by using the id: {}", id);

        Optional<PersonDTO> returned = Optional.empty();
        try {
            PersonDTO found = jdbcTemplate.queryForObject(QUERY_FIND_PERSON_INFORMATION,
                    Collections.singletonMap("id", id),
                    new PersonInformationRowMapper()
            );
            LOGGER.info("Found person information: {}", found);

            returned = Optional.of(found);
        }
        catch (EmptyResultDataAccessException ex) {
            LOGGER.error("No person information found with id: {}", id);
        }

        return returned;
    }

    private static class PersonInformationRowMapper implements RowMapper<PersonDTO> {

        @Override
        public PersonDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            PersonDTO person = new PersonDTO();

            person.setName(rs.getString("name"));
            person.setUserId(rs.getLong("id"));

            return person;
        }
    }
}
