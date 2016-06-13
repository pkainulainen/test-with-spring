package com.testwithspring.starter.assertions.task;

import org.assertj.core.api.SoftAssertions;
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

    /**
     * The problem of this test is that if the status of our task is not
     * correct, the assertion, which verifies that the resolution is null,
     * is not invoked.
     *
     * In other words, we will fix the incorrect status just to find out
     * that the test fails again.
     */
    @Test
    public void build_shouldCreateAnOpenTask() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getStatus())
                .overridingErrorMessage("The status of an open task must be: %s but was: %s",
                        TaskStatus.OPEN,
                        task.getStatus()
                )
                .isEqualTo(TaskStatus.OPEN);

        assertThat(task.getResolution())
                .overridingErrorMessage("An open task cannot have a resolution but had: %s",
                        task.getResolution()
                )
                .isNull();
    }

    /**
     * It's a good idea to use soft assertions if we need to verify multiple
     * conditions that must be true if the object in a specific state.
     */
    @Test
    public void build_shouldCreateAnOpenTaskSoft() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(task.getStatus())
                .overridingErrorMessage("The status of an open task must be: %s but was: %s",
                        TaskStatus.OPEN,
                        task.getStatus()
                )
                .isEqualTo(TaskStatus.OPEN);

        assertions.assertThat(task.getResolution())
                .overridingErrorMessage("An open task cannot have a resolution but had: %s",
                        task.getResolution()
                )
                .isNull();

        assertions.assertAll();
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

    /**
     * The "problem" here is that if one assertion fails, the other assertions are not invoked.
     * This means that the tested code could have other problems that are not exposed by
     * our test method. This is typically solved by splitting assertions into multiple test
     * methods, but we have another option as well.
     */
    @Test
    public void build_ShouldCreateTaskWithCorrectInformation() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        assertThat(task.getId()).isEqualByComparingTo(ID);
        assertThat(task.getAssignee().getUserId()).isEqualByComparingTo(ASSIGNEE_ID);
        assertThat(task.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID);
        assertThat(task.getCloser()).isNull();
        assertThat(task.getTitle()).isEqualTo(TITLE);
        assertThat(task.getDescription()).isEqualTo(DESCRIPTION);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);
        assertThat(task.getResolution())
                .overridingErrorMessage("An open task cannot have a resolution")
                .isNull();
    }

    /**
     * We could use soft assertions here, but the thing is that this test method is not really readable.
     * It is a better idea to split this this into smaller tests that have only one assertion. However,
     * we should keep the assertions which verify that the task is an open task inside one test method,
     * and use soft assertions in that method.
     */
    @Test
    public void build_ShouldCreateTaskWithCorrectInformationSoft() {
        Task task = Task.getBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();

        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(task.getId()).isEqualByComparingTo(ID);
        assertions.assertThat(task.getAssignee().getUserId()).isEqualByComparingTo(ASSIGNEE_ID);
        assertions.assertThat(task.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID);
        assertions.assertThat(task.getCloser()).isNull();
        assertions.assertThat(task.getTitle()).isEqualTo(TITLE);
        assertions.assertThat(task.getDescription()).isEqualTo(DESCRIPTION);

        assertions.assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);
        assertions.assertThat(task.getResolution())
                .overridingErrorMessage("An open task cannot have a resolution")
                .isNull();

        assertions.assertAll();
    }
}
