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
