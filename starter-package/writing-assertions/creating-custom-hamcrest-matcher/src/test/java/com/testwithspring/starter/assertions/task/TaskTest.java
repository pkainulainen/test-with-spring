package com.testwithspring.starter.assertions.task;

import org.junit.Test;

import static com.testwithspring.starter.assertions.task.TaskMatchers.hasTitle;
import static com.testwithspring.starter.assertions.task.TaskMatchers.isOpen;
import static org.junit.Assert.assertThat;

/**
 * This class demonstrates how we can write assertions for "complex" objects
 * by using JUnit 4 and Hamcrest.
 *
 * @author Petri Kainulainen
 */
public class TaskTest {

    private static final Long ID = 1L;
    private static final Long CREATOR_ID = 44L;
    private static final String TITLE = "Write an example project";
    private static final String DESCRIPTION = "Write an example project description";

    @Test
    public void build_shouldCreateAnOpenTask() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task, isOpen());
    }

    @Test
    public void build_shouldCreateTaskWithCorrectTitle() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task, hasTitle(TITLE));
    }
}
