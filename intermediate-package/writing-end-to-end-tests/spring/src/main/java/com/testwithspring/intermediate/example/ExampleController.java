package com.testwithspring.intermediate.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleController.class);

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String renderExampleView() {
        LOGGER.info("Rendering example view");
        return "index";
    }
}
