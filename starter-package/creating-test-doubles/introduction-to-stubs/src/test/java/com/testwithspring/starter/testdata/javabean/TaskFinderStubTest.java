package com.testwithspring.starter.testdata.javabean;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskFinderStubTest {

    private static final Long TASK_ID = 99L;

    private Task found;
    private TaskFinder taskFinder;

    @Before
    public void createStub() {
        found = new TaskBuilder()
                .withId(TASK_ID)
                .build();

        taskFinder = new TaskFinderStub(found);
    }

    @Test
    public void findById_ShouldReturnConfiguredTask() {
        Task returned = taskFinder.findById(TASK_ID);
        assertThat(returned).isSameAs(found);
    }
}
