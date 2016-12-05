package com.testwithspring.intermediate.task;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface TaskRepository extends Repository<Task, Long>, CustomTaskRepository {

    Optional<Task> findOne(Long id);

    Task save(Task task);
}
