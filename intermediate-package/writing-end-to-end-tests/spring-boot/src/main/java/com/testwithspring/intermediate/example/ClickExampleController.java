package com.testwithspring.intermediate.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ClickExampleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClickExampleController.class);

    @RequestMapping(value = "/click-source", method = RequestMethod.GET)
    public String renderClickSourceView() {
        LOGGER.info("Rendering click target page");
        return "click/source";
    }

    @RequestMapping(value = "/click-target", method = RequestMethod.GET)
    public String renderClickTargetView() {
        LOGGER.info("Rendering click target view");
        return "click/target";
    }
}

