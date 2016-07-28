package com.testwithspring.task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * This test class demonstrates how we can verify interactions that
 * happened between the system under test and a spy.
 */
public class MockitoSpyVerifyTest {

    private TaskRepository repository;

    @Before
    public void createSpy() {
        repository = spy(new TaskRepositoryImpl());
    }

    /**
     * This test demonstrates how we can verify that no interactions
     * happened between SUT and a spy.
     */
    @Test
    public void verifyThatNoInteractionsHappenedBetweenSUTAndSpy() {
        verifyZeroInteractions(repository);
    }

    /**
     * This test demonstrates how we can verify that a method was not
     * invoked by using the method parameter {@code 1L}.
     */
    @Test
    public void verifyThatMethodOfSpyWasNotInvoked() {
        verify(repository, never()).findById(1L);
    }

    /**
     * This test demonstrates how we can ensure that a method was called
     * once with the correct method parameters.
     *
     * Note: We shouldn't verify interactions between system under test and
     * our test double if the method is a finder method.
     */
    @Test
    public void verifyWhenMethodWasCalledOnce() {
        repository.findById(1L);

        verify(repository).findById(1L);
    }

    /**
     * This test demonstrates how we can ensure that a method was called once
     * with the correct method parameters.
     *
     * Note: We shouldn't verify interactions between system under test and
     * our test double if the method is a finder method.
     */
    @Test
    public void verifyWhenMethodWasCalledOnceWithoutUsingTheShortCut() {
        repository.findById(1L);

        verify(repository, times(1)).findById(1L);
    }

    /**
     * This test demonstrates how we can ensure that a method was called once
     * with the correct method parameters and that there were no other
     * interactions between SUT and our spy.
     *
     * Note: We shouldn't verify interactions between system under test and
     * our test double if the method is a finder method.
     */
    @Test
    public void verifyThatNoOtherMethodsWereInvoked() {
        repository.findById(1L);

        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository);
    }

    /**
     * This test demonstrates how we can ensure that the method was invoked
     * once and its method parameter was equal to 1L.
     *
     * Note: We shouldn't verify interactions between system under test and
     * our test double if the method is a finder method.
     */
    @Test
    public void verifyThatMethodWasInvokedByAnd1LWasUsedAsMethodParameter() {
        repository.findById(1L);

        verify(repository).findById(eq(1L));
    }

    /**
     * This test demonstrates how we can ensure that the method was invoked
     * once and any {@code Long} object was passed as a method parameter.
     *
     * Note: We shouldn't verify interactions between system under test and
     * our test double if the method is a finder method.
     */
    @Test
    public void verifyThatMethodWasInvokedAndAnyLongObjectWasUsedAsMethodParameter() {
        repository.findById(1L);

        verify(repository).findById(anyLong());
    }

    /**
     * This test demonstrates how we can access the actual method parameter by using
     * an argument captor.
     *
     * Note: We shouldn't verify interactions between system under test and
     * our test double if the method is a finder method.
     */
    @Test
    public void verifyThatMethodWasInvokedByUsingArgumentCaptor() {
        repository.findById(1L);

        ArgumentCaptor<Long> longArgument = ArgumentCaptor.forClass(Long.class);
        verify(repository).findById(longArgument.capture());

        Long actualId = longArgument.getValue();
        assertThat(actualId).isEqualByComparingTo(1L);
    }
}
