package com.testwithspring.starter.springboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    @RequestMapping(value =  "/hello", method = RequestMethod.GET)
    public String sayHello() {
        return "Hello World!";
    }
}
