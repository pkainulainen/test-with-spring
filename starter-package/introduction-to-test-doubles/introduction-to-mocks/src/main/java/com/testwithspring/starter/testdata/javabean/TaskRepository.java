package com.testwithspring.starter.testdata.javabean;

/**
 * Declares the methods used to delete {@code Task} objects.
 */
public interface TaskRepository {

    /**
     * Deletes task whose id is given as a method parameter.
     * @param id    The id of the requested task.
     * @return      The deleted task.
     */
    Task deleteById(Long id);
}
