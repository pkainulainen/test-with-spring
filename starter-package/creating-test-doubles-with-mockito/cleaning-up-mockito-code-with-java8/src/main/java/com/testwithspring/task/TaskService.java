package com.testwithspring.task;

import com.google.common.base.Preconditions;
import com.testwithspring.user.LoggedInUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a copy of an existing task. A copy has the same title and description
     * as the source task.
     *
     * @param sourceTaskId  The id of the source task.
     * @param creator       The authenticated user who is marked as a creator
     *                      created task.
     * @return  The created task.
     */
    public Task createCopyOf(Long sourceTaskId, LoggedInUser creator) {
        Preconditions.checkNotNull(sourceTaskId);
        Preconditions.checkNotNull(creator);

        LOGGER.info("Creating a copy of the task: {} with creator: {}",
                sourceTaskId,
                creator.getUsername()
        );

        Optional<Task> found = repository.findById(sourceTaskId);
        Task source = found.orElseThrow(NotFoundException::new);

        Task created = Task.getBuilder()
                .withCreator(creator.getUserId())
                .withDescription(source.getDescription())
                .withTitle(source.getTitle())
                .build();
        created = repository.save(created);

        LOGGER.info("Created a new task: {}", created);

        return created;
    }
}
