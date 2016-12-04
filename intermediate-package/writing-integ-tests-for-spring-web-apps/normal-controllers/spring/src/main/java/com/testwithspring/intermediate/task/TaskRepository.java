package com.testwithspring.intermediate.task;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface TaskRepository extends Repository<Task, Long> {

    Optional<Task> findOne(Long id);
}
