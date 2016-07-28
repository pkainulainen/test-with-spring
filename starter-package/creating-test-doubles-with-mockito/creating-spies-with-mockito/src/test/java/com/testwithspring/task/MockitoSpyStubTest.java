package com.testwithspring.task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static com.testwithspring.task.TestDoubles.dummy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

/**
 * This test class demonstrates how you can stub the methods
 * of a spy with the "original" Mockito API.
 */
public class MockitoSpyStubTest {

    private TaskRepository repository;

    @Before
    public void createSpy() {
        repository = spy(new TaskRepositoryImpl());
    }

    /**
     * This test method demonstrates how we can configure the
     * returned value when the method in question takes no
     * method parameters.
     */
    @Test
    public void shouldReturnValue() {
        //Won't invoke the real method
        doReturn(1L).when(repository).count();


        //Will invoke the real method.
        //when(repository.count()).thenReturn(1L);

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

        //Won't invoke the real method
        doReturn(Optional.of(expected)).when(repository).findById(1L);

        //Will invoke the real method
        //when(repository.findById(1L)).thenReturn(Optional.of(expected));

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

        //Won't invoke the real method
        doReturn(Optional.of(expected)).when(repository).findById(eq(1L));

        //Will invoke the real method
        //when(repository.findById(eq(1L))).thenReturn(Optional.of(expected));

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

        //Won't invoke the real method
        doReturn(Optional.of(expected)).when(repository).findById(anyLong());

        //Will invoke the real method
        //when(repository.findById(anyLong())).thenReturn(Optional.of(expected));

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

        //Won't invoke the real method
        doAnswer(new Answer<Optional<Task>>() {
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
        }).when(repository).findById(1L);

        //Will invoke the real method
        /*when(repository.findById(1L)).thenAnswer(new Answer<Optional<Task>>() {
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
        });*/

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
        //Won't invoke the real method
        doThrow(new NotFoundException()).when(repository).findById(1L);

        //Will invoke the real method
        //when(repository.findById(1L)).thenThrow(new NotFoundException());

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

        //Won't invoke the real method
        doThrow(new NotFoundException()).when(repository).delete(deleted);

        repository.delete(deleted);
    }
}
