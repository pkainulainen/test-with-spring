package com.testwithspring.intermediate.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides one API method that is used to retrieve the CSRF token.
 */
@RestController
public class CSRFTokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSRFTokenController.class);

    @RequestMapping(value = "/api/csrf", method = RequestMethod.HEAD)
    public void getCsrfToken() {
        LOGGER.info("Getting CSRF token.");
    }
}
