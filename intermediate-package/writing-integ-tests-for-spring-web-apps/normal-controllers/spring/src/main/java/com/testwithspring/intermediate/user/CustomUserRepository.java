package com.testwithspring.intermediate.user;

import java.util.Optional;

/**
 * Declares custom method used to find the person
 * information of an user.
 */
interface CustomUserRepository {

    /**
     * Finds the person information of the user whose id is
     * given as a method parameter.
     * @param id    The id of the requested user.
     * @return      An {@code Optional} that contains the person
     *              information of the found user. If no user is found,
     *              this method returns an empty {@code Optional}.
     */
    Optional<PersonDTO> findPersonInformationById(Long id);
}
