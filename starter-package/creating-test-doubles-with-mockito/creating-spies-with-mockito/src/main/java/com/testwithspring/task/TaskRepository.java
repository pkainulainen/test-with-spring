package com.testwithspring.task;

import java.util.Optional;

interface TaskRepository {

    /**
     * @return The number of tasks found from the database.
     */
    long count();

    /**
     * Deletes task from the database.
     * @param task  The deleted task.
     */
    void delete(Task task);

    /**
     * Finds a task by using its id as a search criteria.
     * @param id    The id of the requested task.
     * @return      An {@code Optional} object that contains the found task.
     *              If no task is found, this method returns an empty
     *              {@code Optional} object.
     */
    Optional<Task> findById(Long id);

    /**
     * Saves a task in the database.
     * @param task  The saved task.
     * @return      The saved task.
     */
    Task save(Task task);
}
