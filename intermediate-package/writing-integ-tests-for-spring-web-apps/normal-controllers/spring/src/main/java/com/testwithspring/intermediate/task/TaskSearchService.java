package com.testwithspring.intermediate.task;

import java.util.List;

/**
 * Declares the method used to find tasks by using
 * a search term.
 */
public interface TaskSearchService {

    /**
     * Finds tasks whose title or description contains the given search term.
     * This search is case insensitive.
     * @param searchTerm    The search term.
     * @return  The found tasks. If no tasks is found, this method returns an
     *          an empty list.
     */
    List<TaskListDTO> search(String searchTerm);
}