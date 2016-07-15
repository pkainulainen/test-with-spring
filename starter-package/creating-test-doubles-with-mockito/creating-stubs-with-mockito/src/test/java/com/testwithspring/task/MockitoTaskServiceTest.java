package com.testwithspring.task;

import com.testwithspring.user.LoggedInUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static com.testwithspring.task.TestDoubles.dummy;
import static com.testwithspring.task.TestDoubles.stub;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

/**
 * This class demonstrates how we can create stubs with the original
 * Mockito API.
 */
public class MockitoTaskServiceTest {

    private final Long CREATOR_ID = 33L;
    private final String CREATOR_USERNAME = "username";
    private final Long NEW_TASK_ID = 120L;
    private final Long SOURCE_TASK_ID = 99L;
    private final String SOURCE_TASK_DESCRIPTION = "description";
    private final String SOURCE_TASK_TITLE = "title";

    private TaskRepository repository;
    private TaskService service;

    @Before
    public void configureSystemUnderTest() {
        repository = stub(TaskRepository.class);
        service = new TaskService(repository);
    }

    @Test(expected = NotFoundException.class)
    public void createCopyOf_WhenSourceTaskIsNotFound_ShouldThrowException() {
        when(repository.findById(SOURCE_TASK_ID)).thenReturn(Optional.empty());

        LoggedInUser notImportant = dummy(LoggedInUser.class);
        service.createCopyOf(SOURCE_TASK_ID, notImportant);
    }

    @Test
    public void createCopyOf_WhenSourceTaskIsFound_ShouldReturnCreatedTask() {
        Task sourceTask = new TaskBuilder()
                .withDescription(SOURCE_TASK_DESCRIPTION)
                .withTitle(SOURCE_TASK_TITLE)
                .build();

        when(repository.findById(SOURCE_TASK_ID)).thenReturn(Optional.of(sourceTask));
        when(repository.save(isA(Task.class))).thenAnswer(new Answer<Task>() {
            @Override
            public Task answer(InvocationOnMock invocation) throws Throwable {
                Task parameter = (Task) invocation.getArguments()[0];
                return new TaskBuilder()
                        .withId(NEW_TASK_ID)
                        .withCreator(parameter.getCreator().getUserId())
                        .withDescription(parameter.getDescription())
                        .withTitle(parameter.getTitle())
                        .build();
            }
        });

        LoggedInUser creator = new LoggedInUser(CREATOR_ID, CREATOR_USERNAME);
        Task created = service.createCopyOf(SOURCE_TASK_ID, creator);

        assertThat(created.getId()).isEqualByComparingTo(NEW_TASK_ID);
        assertThat(created.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID);
        assertThat(created.getDescription()).isEqualTo(SOURCE_TASK_DESCRIPTION);
        assertThat(created.getTitle()).isEqualTo(SOURCE_TASK_TITLE);
    }
}
