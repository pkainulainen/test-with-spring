package com.testwithspring.intermediate.task;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertions;

public final class TaskAssert extends AbstractAssert<TaskAssert, Task> {

    private TaskAssert(Task actual) {
        super(actual, TaskAssert.class);
    }

    public static TaskAssert assertThatTask(Task actual) {
        return new TaskAssert(actual);
    }

    public TaskAssert isOpen() {
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(actual.getCloser())
                .overridingErrorMessage(
                        "The closer of an open task must be null but was %s",
                        actual.getCloser()
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
}
