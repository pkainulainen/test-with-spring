package com.testwithspring.intermediate.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Provides a method that can be used for querying messages from
 * the used database.
 */
@Repository
public class MessageRepository {

    private static final String QUERY_FIND_BY_ID = "SELECT id, message_text " +
            "FROM messages " +
            "WHERE id=:id";

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    MessageRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Find message by using its id as a search criteria.
     * @param id    The id of the requested message.
     * @return      An {@code Optional} that contains the found message.
     *              If no message is found, this method returns an empty
     *              {@code Optional}.
     */
    public Optional<Message> findById(Long id) {
        LOGGER.info("Finding message by using id: {}", id);

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            Message found = jdbcTemplate.queryForObject(QUERY_FIND_BY_ID,
                    params,
                    new BeanPropertyRowMapper<>(Message.class)
            );
            LOGGER.info("Found message: {}", found);

            return Optional.of(found);
        }
        catch (EmptyResultDataAccessException ex) {
            LOGGER.info("No message found with id: {}", id);
            return Optional.empty();
        }
    }
}
