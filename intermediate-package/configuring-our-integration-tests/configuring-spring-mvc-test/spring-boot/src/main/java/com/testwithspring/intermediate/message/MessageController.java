package com.testwithspring.intermediate.message;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @RequestMapping(method = RequestMethod.GET)
    public String getMessage() {
        return "Hello World!";
    }
}
