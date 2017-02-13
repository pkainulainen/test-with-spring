package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.common.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PersonFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonFinder.class);

    private final UserRepository repository;

    @Autowired
    PersonFinder(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds the person information of the user whose id is given as a
     * method parameter.
     * @param userId    The id of the requested user.
     * @return          The found person information.
     * @throws NotFoundException if no person information is found with the provided user id.
     */
    @Transactional(readOnly = true)
    public PersonDTO findPersonInformationByUserId(Long userId) {
        LOGGER.info("Finding person information by using user id: {}", userId);

        PersonDTO found = repository.findPersonInformationById(userId).orElseThrow(
                () -> new NotFoundException(String.format(
                        "No person information found with user id: %d",
                        userId
                ))
        );

        LOGGER.debug("Found person information: {}", found);
        return found;
    }
}
