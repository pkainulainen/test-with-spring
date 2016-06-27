package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskFinderStubTest {

    private static final Long TASK_ID = 99L;

    @Test
    public void findById_ShouldReturnConfiguredTask() {
        Task found = new TaskBuilder()
                .withId(TASK_ID)
                .build();

        TaskFinder taskFinder = new TaskFinderStub(found);

        Task returned = taskFinder.findById(TASK_ID);
        assertThat(returned).isSameAs(found);
    }
}
