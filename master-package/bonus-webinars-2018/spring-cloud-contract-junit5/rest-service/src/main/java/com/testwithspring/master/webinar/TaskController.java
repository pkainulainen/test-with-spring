package com.testwithspring.master.webinar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides a REST API that allows us to
 * get the information of one task.
 */
@RestController
class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final TaskService service;

    TaskController(TaskService service) {
        this.service = service;
    }

    /**
     * Finds the informatioon of the requested task.
     * @param id    The id of the requested task.
     * @return  The information of the found task.
     */
    @GetMapping("/api/task/{id}")
    Task findById(@PathVariable("id") Long id) {
        LOGGER.info("Finding task by id: {}", id);

        Task found = service.findById(id);
        LOGGER.info("Found task: {}", found);

        return found;
    }
}
