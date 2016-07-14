package com.testwithspring.task;

import com.google.common.base.Preconditions;
import com.testwithspring.user.LoggedInUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    public TaskService() {
    }

    public Task createCopyOf(Long sourceTaskId, LoggedInUser creator) {
        Preconditions.checkNotNull(sourceTaskId);
        Preconditions.checkNotNull(creator);

        LOGGER.info("Creating a copy of the task: {} with creator: {}",
                sourceTaskId,
                creator.getUsername()
        );

        //The body of this method is omitted because it is irrelevant for this example.

        return null;
    }
}
