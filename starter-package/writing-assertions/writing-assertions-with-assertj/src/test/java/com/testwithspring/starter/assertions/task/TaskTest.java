package com.testwithspring.starter.assertions.task;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class demonstrates how we can write assertions for "complex" objects
 * by using AssertJ.
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

        assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);
        assertThat(task.getResolution())
                .overridingErrorMessage("An open task cannot have a resolution")
                .isNull();
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

        assertThat(task.getId()).isEqualByComparingTo(ID);
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

        assertThat(task.getAssignee().getUserId()).isEqualByComparingTo(ASSIGNEE_ID);
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

        assertThat(task.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID);
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

        assertThat(task.getCloser()).isNull();
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

        assertThat(task.getTitle()).isEqualTo(TITLE);
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

        assertThat(task.getDescription()).isEqualTo(DESCRIPTION);
    }
}
