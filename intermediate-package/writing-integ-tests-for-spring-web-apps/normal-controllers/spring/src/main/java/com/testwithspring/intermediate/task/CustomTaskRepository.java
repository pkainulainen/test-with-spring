package com.testwithspring.intermediate.task;

import java.util.List;

/**
 * Declares one custom method that is used to find all
 * tasks found from the database.
 */
interface CustomTaskRepository {

    /**
     * Finds all tasks found from the database.
     * @return
     */
    List<TaskListDTO> findAll();

    /**
     * Finds tasks whose title or description contains the given
     * search term.
     * @param searchTerm
     * @return  A list of found tasks. If no tasks is found, this method
     *          returns an empty list.
     */
    List<TaskListDTO> search(String searchTerm);
}
