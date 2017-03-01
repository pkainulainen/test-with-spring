package com.testwithspring.intermediate.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String ShowMessage() {
        LOGGER.info("Rendering show message view");
        return "index";
    }
}
