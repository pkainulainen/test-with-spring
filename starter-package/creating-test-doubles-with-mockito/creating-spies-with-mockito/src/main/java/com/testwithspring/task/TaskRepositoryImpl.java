package com.testwithspring.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * This is a dummy implementation that is used to demonstrate
 * the difference between a mock and spy that were created
 * with Mockito.
 */
public class TaskRepositoryImpl implements TaskRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    public TaskRepositoryImpl() {}

    @Override
    public long count() {
        LOGGER.info("Invoked count() method");
        return 0;
    }

    @Override
    public void delete(Task task) {
        LOGGER.info("Invoked delete() method with task: {}");
    }

    @Override
    public Optional<Task> findById(Long id) {
        LOGGER.info("Invoked findById() method with id: {}", id);
        return Optional.empty();
    }

    @Override
    public Task save(Task task) {
        LOGGER.info("Invoked save() method with task: {}", task);
        return null;
    }
}
