package com.testwithspring.starter.assertions.task;

import org.junit.Before;
import org.junit.Test;

/**
 * This class demonstrates how we can write assertion for the
 * type of the thrown exception.
 *
 * @author Petri Kainulainen
 */
public class TaskFinderAnnotationTest {

    private static final Long ID = 1L;

    private TaskFinder finder;

    @Before
    public void createSystemUnderTest() {
        finder = new TaskFinder();
    }

    /**
     * We can use this technique only if we want to write assertion
     * for the type of the thrown exception AND we don't need to
     * write other assertions.
     */
    @Test(expected = NotFoundException.class)
    public void findById_ShouldThrowException() {
        finder.findById(ID);
    }
}
