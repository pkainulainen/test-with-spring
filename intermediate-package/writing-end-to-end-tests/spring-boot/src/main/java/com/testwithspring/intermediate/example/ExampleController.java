package com.testwithspring.intermediate.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExampleController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String renderExampleView() {
        return "index";
    }
}
