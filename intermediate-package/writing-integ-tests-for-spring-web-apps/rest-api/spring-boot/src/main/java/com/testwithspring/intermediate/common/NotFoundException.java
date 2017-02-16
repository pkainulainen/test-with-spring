package com.testwithspring.intermediate.common;

/**
 * This exception is thrown when the requested information
 * is not found from the database.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
