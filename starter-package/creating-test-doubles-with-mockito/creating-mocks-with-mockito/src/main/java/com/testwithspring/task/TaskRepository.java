package com.testwithspring.task;

import java.util.Optional;

interface TaskRepository {

    Optional<Task> findById(Long id);

    Task save(Task task);
}
