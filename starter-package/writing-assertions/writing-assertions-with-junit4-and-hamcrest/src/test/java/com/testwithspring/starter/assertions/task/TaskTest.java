package com.testwithspring.starter.assertions.task;

import org.junit.Test;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * This class demonstrates how we can write assertions for "complex" objects
 * by using JUnit 4 and Hamcrest.
 *
 * @author Petri Kainulainen
 */
public class TaskTest {

    private static final Long ID = 1L;
    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CREATOR_ID = 44L;
    private static final String TITLE = "Write an example project";
    private static final String DESCRIPTION = "Write an example project description";

    @Test
    public void build_shouldCreateAnOpenTask() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getStatus(), is(TaskStatus.OPEN));
        assertThat("An open task cannot have a resolution", task.getResolution(), nullValue());
    }

    @Test
    public void build_shouldSetId() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getId(), is(ID));
    }

    @Test
    public void build_shouldSetAssigneeWithCorrectId() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getAssignee().getUserId(), is(ASSIGNEE_ID));
    }

    @Test
    public void build_shouldSetCreatorWithCorrectId() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getCreator().getUserId(), is(CREATOR_ID));
    }

    @Test
    public void build_shouldNotSetCloser() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getCloser(), nullValue());
    }

    @Test
    public void build_shouldSetTitle() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getTitle(), is(TITLE));
    }

    @Test
    public void build_shouldSetDescription() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getDescription(), is(DESCRIPTION));
    }

    @Test
    public void build_shouldCreateTaskWithCorrectFieldValues() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task, allOf(
                hasProperty("id", is(ID)),
                hasProperty("assignee", hasProperty("userId", is(ASSIGNEE_ID))),
                hasProperty("closer", nullValue()),
                hasProperty("creator", hasProperty("userId", is(CREATOR_ID))),
                hasProperty("title", is(TITLE)),
                hasProperty("description", is(DESCRIPTION)),
                hasProperty("status", is(TaskStatus.OPEN)),
                hasProperty("resolution", nullValue())
        ));
    }

    @Test
    public void build_shouldCreateTaskWithAtLeastOneCorrectFieldValue() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task, anyOf(
                hasProperty("id", is(ID)),
                hasProperty("assignee", hasProperty("userId", is(ASSIGNEE_ID))),
                hasProperty("closer", nullValue()),
                hasProperty("creator", hasProperty("userId", is(CREATOR_ID))),
                hasProperty("title", is(TITLE)),
                hasProperty("description", is(DESCRIPTION)),
                hasProperty("status", is(TaskStatus.OPEN)),
                hasProperty("resolution", nullValue())
        ));
    }
}
