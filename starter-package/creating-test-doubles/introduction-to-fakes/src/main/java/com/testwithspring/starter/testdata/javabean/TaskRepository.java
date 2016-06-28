package com.testwithspring.starter.testdata.javabean;

/**
 * Declares the methods used to save and query {@code Task} objects.
 */
public interface TaskRepository {

    /**
     * Returns the task whose id is given as a method parameter.
     * @param id    The id of the requested task.
     * @return  The found task.
     */
    Task findById(Long id);

    /**
     * Saves the task that is given as a method parameter.
     * @param saved The saved task.
     * @return  The saved task.
     */
    Task save(Task saved);
}
