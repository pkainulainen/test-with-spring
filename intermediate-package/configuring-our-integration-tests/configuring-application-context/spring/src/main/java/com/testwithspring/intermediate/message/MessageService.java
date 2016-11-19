package com.testwithspring.intermediate.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository repository;

    @Autowired
    MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public String getMessage() {
        LOGGER.info("Returning message.");

        String message = repository.findMessage();

        LOGGER.info("Found message: {}", message);
        return message;
    }
}
