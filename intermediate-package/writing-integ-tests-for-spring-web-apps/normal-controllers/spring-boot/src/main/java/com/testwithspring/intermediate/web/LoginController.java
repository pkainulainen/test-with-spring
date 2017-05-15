package com.testwithspring.intermediate.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller is responsible of rendering the login
 * page.
 */
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private static final String VIEW_NAME_LOGIN = "user/login";

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public String showLoginPage() {
        LOGGER.info("Rendering login page");
        return VIEW_NAME_LOGIN;
    }
}
