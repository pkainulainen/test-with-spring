package com.testwithspring.starter.assertions.task;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class demonstrates how we can create a custom assertion for {@code Task}
 * objects and transform our assertions into a domain-specific language.
 */
public class TaskAssert extends AbstractAssert<TaskAssert, Task> {

    private TaskAssert(Task actual) {
        super(actual, TaskAssert.class);
    }

    public static TaskAssert assertThatTask(Task actual) {
        return new TaskAssert(actual);
    }

    public TaskAssert hasDescription(String description) {
        assertThat(actual.getDescription())
                .overridingErrorMessage("Expected description to be: %s but was: %s",
                        description,
                        actual.getDescription()
                )
                .isEqualTo(description);

        return this;
    }

    public TaskAssert hasId(Long id) {
        assertThat(actual.getId())
                .overridingErrorMessage("Expected id to be: %d but was: %d",
                        id,
                        actual.getId()
                )
                .isEqualByComparingTo(id);

        return this;
    }

    public TaskAssert hasTitle(String title) {
        assertThat(actual.getTitle())
                .overridingErrorMessage("Expected title to be: %s but was: %s",
                        title,
                        actual.getTitle()
                )
                .isEqualTo(title);

        return this;
    }

    public TaskAssert isAssignedTo(Long assigneeId) {
        Assignee actualAssignee = actual.getAssignee();

        assertThat(actualAssignee.getUserId())
                .overridingErrorMessage("Expected that the task is assigned to user: %d but is assigned to user: %d",
                        assigneeId,
                        actualAssignee.getUserId()
                )
                .isEqualByComparingTo(assigneeId);

        return this;
    }

    /**
     * Ensures that the asserted task is in progress. This means that:
     * <ul>
     *     <li>Its {@code status} must be {@code TaskStatus.IN_PROGRESS}.</li>
     *     <li>Its {@code resolution} must be {@code null}.</li>
     * </ul>
     * @return
     */
    public TaskAssert isInProgress() {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(actual.getStatus())
                .overridingErrorMessage("The status of a task that is in progress must be: %s but was: %s",
                        TaskStatus.IN_PROGRESS,
                        actual.getStatus()
                )
                .isEqualTo(TaskStatus.IN_PROGRESS);

        assertions.assertThat(actual.getResolution())
                .overridingErrorMessage("A task that is in progress cannot have a resolution but had: %s",
                        actual.getResolution()
                )
                .isNull();

        assertions.assertAll();

        return this;
    }

    /**
     * Ensures that the asserted task is open. This means that:
     * <ul>
     *     <li>Its {@code status} must be {@code TaskStatus.OPEN}.</li>
     *     <li>Its {@code resolution} must be {@code null}.</li>
     * </ul>
     * @return
     */
    public TaskAssert isOpen() {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(actual.getStatus())
                .overridingErrorMessage("The status of an open task must be: %s but was: %s",
                        TaskStatus.OPEN,
                        actual.getStatus()
                )
                .isEqualTo(TaskStatus.OPEN);

        assertions.assertThat(actual.getResolution())
                .overridingErrorMessage("An open task cannot have a resolution but had: %s",
                        actual.getResolution()
                )
                .isNull();

        assertions.assertAll();

        return this;
    }

    /**
     * Ensures that the task was closed with resolution done by the specified user. This mean that:
     * <ul>
     *     <li>The id of the closer must be to the id given as a method parameter.</li>
     *      <li>Its {@code status} must be {@code TaskStatus.CLOSED}.</li>
     *     <li>Its {@code resolution} must be {@code TaskResolution.DONE}.</li>
     * </ul>
     * @param closerId
     * @return
     */
    public TaskAssert wasClosedWithResolutionDoneBy(Long closerId) {
        assertThatWasClosedWithResolutionBy(TaskResolution.DONE, closerId);
        return this;
    }

    /**
     * Ensures that the task was closed with resolution done by the specified user. This mean that:
     * <ul>
     *     <li>The id of the closer must be to the id given as a method parameter.</li>
     *      <li>Its {@code status} must be {@code TaskStatus.CLOSED}.</li>
     *     <li>Its {@code resolution} must be {@code TaskResolution.DUPLICATE}.</li>
     * </ul>
     * @param closerId
     * @return
     */
    public TaskAssert wasClosedWithResolutionDuplicateBy(Long closerId) {
        assertThatWasClosedWithResolutionBy(TaskResolution.DUPLICATE, closerId);
        return this;
    }

    /**
     * Ensures that the task was closed with resolution won't do by the specified user. This mean that:
     * <ul>
     *     <li>The id of the closer must be to the id given as a method parameter.</li>
     *      <li>Its {@code status} must be {@code TaskStatus.CLOSED}.</li>
     *     <li>Its {@code resolution} must be {@code TaskResolution.WONT_DO}.</li>
     * </ul>
     * @param closerId
     * @return
     */
    public TaskAssert wasClosedWithResolutionWontDoBy(Long closerId) {
        assertThatWasClosedWithResolutionBy(TaskResolution.WONT_DO, closerId);
        return this;
    }

    private void assertThatWasClosedWithResolutionBy(TaskResolution resolution, Long closerId) {
        SoftAssertions assertions = new SoftAssertions();

        Closer actualCloser = actual.getCloser();
        assertions.assertThat(actualCloser.getUserId())
                .overridingErrorMessage("Expected that the task was closed by user: %d but was closed by user: %d",
                        closerId,
                        actualCloser.getUserId()
                )
                .isEqualByComparingTo(closerId);

        assertions.assertThat(actual.getStatus())
                .overridingErrorMessage("The status of a closed task must be: %s but was: %s",
                        TaskStatus.CLOSED,
                        actual.getStatus()
                )
                .isEqualTo(TaskStatus.CLOSED);

        assertions.assertThat(actual.getResolution())
                .overridingErrorMessage("Expected the resolution to be: %s but was: %s",
                        resolution,
                        actual.getResolution()
                )
                .isEqualTo(resolution);

        assertions.assertAll();
    }

    public TaskAssert wasCreatedBy(Long creatorId) {
        Creator actualCreator = actual.getCreator();

        assertThat(actualCreator.getUserId())
                .overridingErrorMessage("Expected that the task was created by user: %d but was created by user: %d",
                        creatorId,
                        actualCreator.getUserId()
                )
                .isEqualByComparingTo(creatorId);

        return this;
    }
}
