package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.message.MessageDTO;
import com.testwithspring.intermediate.message.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/message")
class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private final MessageService service;

    @Autowired
    MessageController(MessageService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<MessageDTO> findAll() {
        LOGGER.info("Finding all messages from the database");

        List<MessageDTO> messages = service.findAll();
        LOGGER.info("Found {} messages", messages.size());

        return messages;
    }
}
