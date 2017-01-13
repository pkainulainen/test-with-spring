package com.testwithspring.intermediate.task;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;

public final class TaskDTOAssert extends AbstractAssert<TaskDTOAssert, TaskDTO> {

    private TaskDTOAssert(TaskDTO actual) {
        super(actual, TaskDTOAssert.class);
    }

    public static TaskDTOAssert assertThatTask(TaskDTO actual) {
        return new TaskDTOAssert(actual);
    }

    public TaskDTOAssert isOpen() {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(actual.getCloserId())
                .overridingErrorMessage(
                        "The closer of an open task must be null but was %d",
                        actual.getCloserId()
                )
                .isNull();

        assertions.assertThat(actual.getStatus())
                .overridingErrorMessage(
                        "The status of an open task must be: %s but was: %s",
                        TaskStatus.OPEN,
                        actual.getStatus()
                )
                .isEqualTo(TaskStatus.OPEN);

        assertions.assertThat(actual.getResolution())
                .overridingErrorMessage(
                        "An open task cannot have a resolution but had: %s",
                        actual.getResolution()
                )
                .isNull();

        assertions.assertAll();

        return this;
    }

    public void wasClosedWithResolutionDoneBy(Long closerId) {
        SoftAssertions assertions = new SoftAssertions();

        Long actualCloser = actual.getCloserId();
        assertions.assertThat(actualCloser)
                .overridingErrorMessage("Expected that the task was closed by user: %d but was closed by user: %d",
                        closerId,
                        actualCloser
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
                        TaskResolution.DONE,
                        actual.getResolution()
                )
                .isEqualTo(TaskResolution.DONE);

        assertions.assertAll();
    }

}
