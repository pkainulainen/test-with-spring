package com.testwithspring.master.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

class MessageRepositoryImpl implements MessageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRepositoryImpl.class);

    MessageRepositoryImpl() {}

    @Override
    public Optional<Message> findById(Long id) {
        LOGGER.info("Finding message by id: {}", id);

        Message found = new Message();
        found.setId(id);
        found.setMessageText("Hello from Spy!");

        return Optional.of(found);
    }

    @Override
    public Message save(Message message) {
        LOGGER.info("Saving a message with information: {}", message);

        message.setId(3L);

        return message;
    }
}
