package com.testwithspring.starter.testdata.javabean;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a working fake implementation for the
 * {@code TaskRepository} interface. This implementation uses
 * a {@code Map} object as a data storage.
 */
public final class TaskRepositoryFake implements TaskRepository {

    private long nextId;
    private Map<Long, Task> tasks;

    public TaskRepositoryFake() {
        nextId = 1L;
        tasks = new HashMap<>();
    }

    @Override
    public Task findById(Long id) {
        return tasks.get(id);
    }

    @Override
    public Task save(Task saved) {
        long id = nextId;

        saved.setId(id);
        tasks.put(id, saved);

        nextId++;

        return saved;
    }
}
