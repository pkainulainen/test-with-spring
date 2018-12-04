package com.testwithspring.master.webinar;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides a finder method that returns the information
 * of one task.
 */
@Service
class TaskService {

    private final Map<Long, Task> tasks;

    TaskService() {
        tasks = new HashMap<>();
        tasks.put(1L, new Task(1L, "Write REST service"));
        tasks.put(2L, new Task(2L, "Write REST client"));
    }

    /**
     * Finds the information of the requested task.
     * @param id    The id of the requested task.
     * @return  The found task. If no task is found,
     *          this method returns {@code null}
     */
    Task findById(Long id) {
        return tasks.get(id);
    }
}
