package com.testwithspring.master.task;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface TaskRepository extends Repository<Task, Long>, CustomTaskRepository {

    void delete(Task deleted);

    void flush();

    Optional<Task> findById(Long id);

    Task save(Task task);
}
