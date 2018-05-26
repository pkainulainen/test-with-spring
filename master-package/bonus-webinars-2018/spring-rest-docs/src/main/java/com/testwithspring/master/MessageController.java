package com.testwithspring.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/message")
class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    MessageDTO create(@Valid @RequestBody MessageDTO message) {
        LOGGER.info("Creating a new message with information: {}", message);
        //Note that I return the method parameter only because this is an example.
        return message;
    }
}
