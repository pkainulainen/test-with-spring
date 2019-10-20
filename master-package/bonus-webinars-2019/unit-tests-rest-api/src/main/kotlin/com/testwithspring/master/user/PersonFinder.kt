package com.testwithspring.master.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * This component isn't meant to be used in
 * production. This component exists only
 * because it allows us to compile our example
 * application and write unit tests for it.
 */
@Component
open class PersonFinder {

    /**
     * Finds the person information information of the specified user.
     * @param   userId  The user id of the user who information will be returned.
     * @return  The "found" person information with a hard coded name.
     */
    @Transactional(readOnly = true)
    open fun findPersonInformationByUserId(userId: Long): PersonDTO {
        return PersonDTO(name = "Hard Coded", userId = userId)
    }
}