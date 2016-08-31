package com.testwithspring.starter.springboot.task;

import java.util.List;

/**
 * Provides CRUD operations for tasks.
 */
public interface TaskCrudService {

    /**
     * Creates a new task and saves it in the database.
     * @param task  The information of the new task.
     * @return      The information of the created task.
     */
    public TaskDTO create(TaskFormDTO task);

    /**
     * Deletes an existing task.
     * @param id    The id of the deleted task.
     * @return      The information of the deleted task.
     * @throws TaskNotFoundException    If the deleted task is not found.
     */
    public TaskDTO delete(Long id);

    /**
     * Finds all tasks found from the database.
     * @return  A list of tasks found from the database.
     */
    public List<TaskListDTO> findAll();

    /**
     * Finds an existing task.
     * @param id    The id of the requested task.
     * @return      The information of the found task.
     * @throws TaskNotFoundException    If no task is found with the given id.
     */
    public TaskDTO findById(Long id);

    /**
     * Updates the information of an existing task.
     * @param task  The new information of an existing task.
     * @return      The information of the updated task.
     */
    public TaskDTO update(TaskFormDTO task);
}
