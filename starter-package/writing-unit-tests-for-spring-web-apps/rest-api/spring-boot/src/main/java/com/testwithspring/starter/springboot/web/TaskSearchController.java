package com.testwithspring.starter.springboot.web;

import com.testwithspring.starter.springboot.task.TaskListDTO;
import com.testwithspring.starter.springboot.task.TaskSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller implements the search function.
 */
@RestController
public class TaskSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskSearchController.class);

    private final TaskSearchService service;

    @Autowired
    TaskSearchController(TaskSearchService service) {
        this.service = service;
    }

    /**
     * Search tasks that fulfill the given search criteria.
     * @param searchTerm    The search term.
     * @return  A list that contains the found tasks. If no tasks is found, this method
     * returns an empty list.
     */
    @RequestMapping(value = "/api/task/search", method = RequestMethod.GET)
    public List<TaskListDTO> search(@RequestParam("searchTerm") String searchTerm) {
        LOGGER.info("Finding tasks by using a search term: {}", searchTerm);

        List<TaskListDTO> results = service.search(searchTerm);
        LOGGER.info("Found {} tasks by using a search term: {}", results.size(), searchTerm);

        return results;
    }
}
