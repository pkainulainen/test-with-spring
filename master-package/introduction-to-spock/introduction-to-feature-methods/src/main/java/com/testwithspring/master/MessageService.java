package com.testwithspring.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a simple message service that returns
 * hard-coded message back to the object that invokes
 * the {@code getMessage()} method.
 */
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public String getMessage() {
        LOGGER.info("Returning message: Hello World!");
        return "Hello World!";
    }
}
