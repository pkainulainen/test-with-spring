package com.testwithspring.intermediate.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonFinder.class);

    private final UserRepository repository;

    @Autowired
    PersonFinder(UserRepository repository) {
        this.repository = repository;
    }
}
