package com.testwithspring.starter.assertions.task;

import org.junit.Test;

import static com.testwithspring.starter.assertions.task.TaskAssert.assertThatTask;

/**
 * This class demonstrates how we can write assertions for "complex" objects
 * by using AssertJ.
 *
 * @author Petri Kainulainen
 */
public class TaskTest {

    private static final Long ID = 1L;
    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CLOSER_ID = 55L;
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

        assertThatTask(task).isOpen();
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

        assertThatTask(task).hasId(ID);
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

        assertThatTask(task).isAssignedTo(ASSIGNEE_ID);
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

       assertThatTask(task).wasCreatedBy(CREATOR_ID);
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

        assertThatTask(task).hasTitle(TITLE);
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

        assertThatTask(task).hasDescription(DESCRIPTION);
    }

    @Test
    public void closeWithResolutionDone_ShouldCloseTaskWithResolutionDoneAndSetCloser() {
        Task task = new TaskBuilder()
                .withStatusOpen()
                .build();
        task.closeWithResolutionDone(CLOSER_ID);

        assertThatTask(task).wasClosedWithResolutionDoneBy(CLOSER_ID);
    }

    @Test
    public void closeWithResolutionDuplicate_ShouldCloseTaskWithResolutionDuplicateAndSetCloser() {
        Task task = new TaskBuilder()
                .withStatusOpen()
                .build();
        task.closeWithResolutionDuplicate(CLOSER_ID);

        assertThatTask(task).wasClosedWithResolutionDuplicateBy(CLOSER_ID);
    }

    @Test
    public void closeWithResolutionWontDo_ShouldCloseTaskWithResolutionWontDoAndSetCloser() {
        Task task = new TaskBuilder()
                .withStatusOpen()
                .build();
        task.closeWithResolutionWontDo(CLOSER_ID);

        assertThatTask(task).wasClosedWithResolutionWontDoBy(CLOSER_ID);
    }

    @Test
    public void startProgress_ShouldChangeStatusToInProgress() {
        Task task = new TaskBuilder()
                .withStatusOpen()
                .build();
        task.startProgress();

        assertThatTask(task).isInProgress();
    }
}
