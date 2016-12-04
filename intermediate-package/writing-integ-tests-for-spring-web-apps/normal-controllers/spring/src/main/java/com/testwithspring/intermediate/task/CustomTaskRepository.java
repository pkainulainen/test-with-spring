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
}
