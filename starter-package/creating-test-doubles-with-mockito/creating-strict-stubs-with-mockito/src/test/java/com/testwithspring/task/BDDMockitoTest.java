package com.testwithspring.task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static com.testwithspring.task.TestDoubles.dummy;
import static com.testwithspring.task.TestDoubles.strictStub;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;

/**
 * This test class demonstrates how we can configure the behavior of a stub
 * by using BDDMockito.
 */
public class BDDMockitoTest {

    private TaskRepository repository;

    @Before
    public void createStrictStub() {
        repository = strictStub(TaskRepository.class);
    }

    /**
     * This test method demonstrates how we can configure the
     * returned value when the method in question takes no
     * method parameters.
     */
    @Test
    public void shouldReturnValue() {
        willReturn(1L).given(repository).count();

        long actualCount = repository.count();

        assertThat(actualCount).isEqualByComparingTo(1L);
    }

    /**
     * This test method demonstrates how we can configure
     * the returned object when the method takes method parameters
     * and we can verify the equality of actual and expected
     * method parameters by using the {@code equals()} method.
     */
    @Test
    public void shouldReturnObject() {
        Task expected = dummy(Task.class);
        willReturn(Optional.of(expected)).given(repository).findById(1L);

        Optional<Task> actual = repository.findById(1L);

        assertThat(actual).containsSame(expected);
    }

    /**
     * This test method demonstrates how we can configure
     * the returned object when the method takes method parameters
     * and we verify the equality of actual and expected
     * method parameters by using an argument matcher.
     */
    @Test
    public void shouldReturnObjectWhenExpectedParameterIsEqualToActualParameter() {
        Task expected = dummy(Task.class);
        willReturn(Optional.of(expected)).given(repository).findById(eq(1L));

        Optional<Task> actual = repository.findById(1L);

        assertThat(actual).containsSame(expected);
    }

    /**
     * This test method demonstrates how we can configure
     * the returned object when the method takes method parameters
     * and we verify the equality of actual and expected
     * method parameters by using an argument matcher.
     */
    @Test
    public void shouldReturnObjectWhenParameterIsAnyLong() {
        Task expected = dummy(Task.class);
        willReturn(Optional.of(expected)).given(repository).findById(anyLong());

        Optional<Task> actual = repository.findById(1L);

        assertThat(actual).containsSame(expected);
    }

    /**
     * This test method describes how we can configure the response
     * of a stubbed method by using an {@code Answer} object.
     */
    @Test
    public void shouldReturnObjectByUsingAnswer() {
        Task expected = dummy(Task.class);
        willAnswer(new Answer<Optional<Task>>() {
            @Override
            public Optional<Task> answer(InvocationOnMock invocation) throws Throwable {
                Long idParameter = (Long) invocation.getArguments()[0];
                if (idParameter.equals(1L)) {
                    return Optional.of(expected);
                }
                else {
                    return Optional.empty();
                }
            }
        }).given(repository).findById(1L);

        Optional<Task> actual = repository.findById(1L);

        assertThat(actual).containsSame(expected);
    }

    /**
     * This test method demonstrates how we can configure the stubbed
     * method to throw an exception when the stubbed method has a return
     * value.
     */
    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenMethodHasReturnValue() {
        willThrow(new NotFoundException()).given(repository).findById(1L);

        repository.findById(1L);
    }

    /**
     * This test method demonstrates how we can configure the stubbed
     * method to throw an exception when the stubbed method doesn't
     * have a method value.
     */
    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenMethodHasNoReturnValue() {
        Task deleted = dummy(Task.class);
        willThrow(new NotFoundException()).given(repository).delete(deleted);

        repository.delete(deleted);
    }
}
