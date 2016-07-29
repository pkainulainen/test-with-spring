package com.testwithspring.task;

import org.junit.Before;
import org.junit.Test;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * This test class demonstrates how we can capture the actual
 * method parameter by using Java 8 and Mockito.
 */
public class MockitoJava8ArgumentCaptorTest {

    private TaskRepository repository;

    @Before
    public void createMock() {
        repository = mock(TaskRepository.class);
    }

    @Test
    public void verifyThatMethodWasInvokedAndCaptureMethodParameter() {
        repository.findById(1L);

        verify(repository).findById(assertArg(
                argument -> assertThat(argument).isEqualByComparingTo(1L)
        ));
    }
}
