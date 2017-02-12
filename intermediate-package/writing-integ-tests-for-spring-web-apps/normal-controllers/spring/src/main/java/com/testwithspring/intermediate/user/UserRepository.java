package com.testwithspring.intermediate.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    Optional<User> findByEmailAddress(String emailAddress);
}
