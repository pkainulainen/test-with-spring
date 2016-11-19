package com.testwithspring.intermediate.message;

import org.springframework.stereotype.Repository;

@Repository
class MessageRepository {

    String findMessage() {
        return "Hello World!";
    }
}
