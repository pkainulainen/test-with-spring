package com.testwithspring.starter.testdata.javabean;

/**
 * Declares the methods used to find {@code Task} objects.
 */
public interface TaskFinder {

    /**
     * Returns the task whose id is given as a method parameter.
     * @param id    The id of the requested task.
     * @return      The found task or {@code null} if no task is found.
     */
    Task findById(Long id);
}
