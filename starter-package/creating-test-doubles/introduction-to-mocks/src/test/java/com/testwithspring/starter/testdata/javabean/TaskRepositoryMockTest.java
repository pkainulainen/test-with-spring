package com.testwithspring.starter.testdata.javabean;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskRepositoryMockTest {

    private static final Long TASK_ID = 44L;

    private TaskRepositoryMock repository;

    @Before
    public void createMock() {
        repository = new TaskRepositoryMock();
    }

    @Test
    public void deleteById_ShouldInvokeCorrectMethodWithCorrectId() {
        repository.setExpectedIdArgument(TASK_ID);
        repository.deleteById(TASK_ID);
        repository.verify();
    }

    @Test
    public void deleteById_ShouldReturnDeletedTask() {
        Task deleted = new TaskBuilder()
                .withId(TASK_ID)
                .build();

        repository.setReturnedTask(deleted);

        Task returned = repository.deleteById(TASK_ID);
        assertThat(returned).isSameAs(deleted);
    }
}
