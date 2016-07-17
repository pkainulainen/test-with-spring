package com.testwithspring.task;

import java.util.Optional;

interface TaskRepository {

    long count();

    void delete(Task task);

    Optional<Task> findById(Long id);

    Task save(Task task);
}
