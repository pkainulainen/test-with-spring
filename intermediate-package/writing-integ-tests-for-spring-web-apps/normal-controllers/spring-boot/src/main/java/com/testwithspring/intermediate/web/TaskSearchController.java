package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.task.TaskListDTO;
import com.testwithspring.intermediate.task.TaskSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * This controllers processes the form submissions of the search
 * task form.
 */
@Controller
public class TaskSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskSearchController.class);

    private static final String MODEL_ATTRIBUTE_NAME_SEARCH_TERM = "searchTerm";
    private static final String MODEL_ATTRIBUTE_NAME_TASKS = "tasks";
    private static final String VIEW_NAME_SEARCH_RESULTS = "task/search-results";

    private final TaskSearchService service;

    @Autowired
    TaskSearchController(TaskSearchService service) {
        this.service = service;
    }

    /**
     * Search tasks that fulfill the given search criteria and renders
     * the search result form.
     * @param searchTerm
     * @param model
     * @return  The name of the view search results view.
     */
    @RequestMapping(value = "task/search", method = RequestMethod.POST)
    public String showSearchResults(@RequestParam("searchTerm") String searchTerm,
                                    Model model) {
        LOGGER.info("Finding tasks by using a search term: {}", searchTerm);

        List<TaskListDTO> searchResults = service.search(searchTerm);
        LOGGER.info("Found {} tasks by using a search term: {}", searchResults.size(), searchTerm);

        model.addAttribute(MODEL_ATTRIBUTE_NAME_SEARCH_TERM, searchTerm);
        model.addAttribute(MODEL_ATTRIBUTE_NAME_TASKS, searchResults);

        return VIEW_NAME_SEARCH_RESULTS;
    }
}
