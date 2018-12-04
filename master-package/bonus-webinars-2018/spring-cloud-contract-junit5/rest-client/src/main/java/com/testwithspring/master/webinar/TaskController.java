package com.testwithspring.master.webinar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Provides a REST API that allows us to
 * get the information of one task.
 */
@RestController
class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    private final RestTemplate restTemplate;

    TaskController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Finds the informatioon of the requested task.
     * @param id    The id of the requested task.
     * @return  The information of the found task.
     */
    @GetMapping("/api/task/{id}")
    Task findById(@PathVariable("id") Long id) {
        LOGGER.info("Finding task by id: {}", id);

        Task found = restTemplate.getForObject("http://localhost:8080/api/task/{id}", Task.class, id);
        LOGGER.info("Found task: {}", found);

        return found;
    }
}
