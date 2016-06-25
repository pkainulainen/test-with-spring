package com.testwithspring.starter.assertions.task;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

/**
 * This class demonstrates how we can write assertions for the exception that
 * is thrown by the system under test.
 *
 * We should use the {@code ExpectedException} rule for this purpose if we
 * cannot use AssertJ or we don't use Java 8.
 *
 * @author Petri Kainulainen
 */
public class TaskFinderRuleTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final Long ID = 1L;

    private TaskFinder finder;

    @Before
    public void createSystemUnderTest() {
        finder = new TaskFinder();
    }

    @Test
    public void findById_ShouldThrowException() {
        thrown.expect(NotFoundException.class);
        finder.findById(ID);
    }

    @Test
    public void findById_ShouldThrowExceptionThatHasCorrectId() {
        thrown.expect(hasProperty("id", is(ID)));
        finder.findById(ID);
    }

    @Test
    public void findById_ShouldThrowExceptionThatHasCorrectMessage() {
        thrown.expect(hasProperty("message", is("No entity found with id: 1")));
        finder.findById(ID);
    }
}
