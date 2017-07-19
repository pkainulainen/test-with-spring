package com.testwithspring.master.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides CRUD operations for messages.
 */
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds a message by using its id as a search criteria.
     * @param id    The id of the retrieved message.
     * @return      The found message.
     * @throws NotFoundException if no message is found with the given id.
     */
    public Message findById(Long id) {
        LOGGER.info("Finding message by id: {}", id);

        Message found = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(
                        "No message found with id: %d",
                        id
                ))
        );
        LOGGER.info("Found message: {}", found);

        return found;
    }

    /**
     * Saves a new message to the database.
     * @param message   The saved message.
     * @return          The saved message.
     */
    public Message create(Message message) {
        LOGGER.info("Saving a new message: {}", message);

        Message saved = repository.save(message);
        LOGGER.info("Saved message: {}", message);

        return saved;
    }
}
