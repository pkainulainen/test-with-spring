package com.testwithspring.intermediate.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    /**
     * Finds the user who has the email address given as a method parameter.
     * @param emailAddress
     * @return  An {@code Optional} object that contains the found user. If no use
     *          is found, this method returns an empty {@code Optional} object.
     */
    Optional<User> findByEmailAddress(String emailAddress);
}
