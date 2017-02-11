package com.testwithspring.intermediate.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface UserRepository extends Repository<User, Long> {

    Optional<User> findByUsername(String username);
}
