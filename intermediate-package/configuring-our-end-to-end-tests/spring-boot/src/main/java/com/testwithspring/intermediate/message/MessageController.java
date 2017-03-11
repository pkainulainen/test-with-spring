package com.testwithspring.intermediate.message;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MessageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showMessage() {
        return "index";
    }
}
