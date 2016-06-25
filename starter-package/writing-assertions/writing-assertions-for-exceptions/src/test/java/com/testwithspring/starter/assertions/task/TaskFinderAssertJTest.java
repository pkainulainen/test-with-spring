package com.testwithspring.starter.assertions.task;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * This class demonstrates how we can write assertions for the exception that
 * is thrown by the system under test.
 *
 * We should use AssertJ for this purpose if we are already using it AND
 * we use Java 8.
 *
 * @author Petri Kainulainen
 */
public class TaskFinderAssertJTest {

    private static final Long ID = 1L;

    private TaskFinder finder;

    @Before
    public void createSystemUnderTest() {
        finder = new TaskFinder();
    }

    @Test
    public void findById_ShouldThrowException() {
        Throwable thrown = catchThrowable(() -> finder.findById(ID));
        assertThat(thrown).isExactlyInstanceOf(NotFoundException.class);
    }

    @Test
    public void findById_ShouldThrowExceptionThatHasCorrectId() {
        Throwable thrown = catchThrowable(() -> finder.findById(ID));
        NotFoundException notFound = (NotFoundException) thrown;

        assertThat(notFound.getId()).isEqualByComparingTo(ID);
    }

    @Test
    public void findById_ShouldThrowExceptionThatHasCorrectMessage() {
        Throwable thrown = catchThrowable(() -> finder.findById(ID));
        assertThat(thrown.getMessage()).isEqualTo("No entity found with id: 1");
    }
}
