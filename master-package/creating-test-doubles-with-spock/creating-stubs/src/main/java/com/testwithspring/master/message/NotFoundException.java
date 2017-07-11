package com.testwithspring.master.message;

/**
 * This exception is thrown when no message is found.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
