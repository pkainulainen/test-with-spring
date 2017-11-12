package com.testwithspring.intermediate.message;

import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Declares a single method that return tasks found from our database.
 */
interface MessageRepository extends Repository<Message, Long> {

    List<Message> findAll();
}
