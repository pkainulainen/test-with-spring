package com.testwithspring.intermediate.message;

class MessageService {

    private final String message;

    MessageService(String message) {
        this.message = message;
    }

    String getMessage() {
        return message;
    }
}
