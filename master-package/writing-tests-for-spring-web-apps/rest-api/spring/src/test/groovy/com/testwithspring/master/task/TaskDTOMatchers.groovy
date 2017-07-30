package com.testwithspring.master.task

import org.hamcrest.TypeSafeDiagnosingMatcher

/**
 * Provides custom Hamcrest matchers that are used to write assertions
 * for {@link TaskDTO} objects.
 */
final class TaskDTOMatchers {

    /**
     * Ensures that the task is open. In other words, this matcher
     * ensures that the status of the task is: {@link TaskStatus#OPEN}
     * and that its resolution is {@code null}.
     * @return
     */
    def static isOpen() {
        return [
                matchesSafely: {task, mismatchDescription ->
                    mismatchDescription.appendText(' but had status: ')
                            .appendValue(task.status)
                            .appendText(' and resolution: ')
                            .appendValue(task.resolution)
                    (task.closer == null) && (task.status == TaskStatus.OPEN) && (task.resolution == null)
                },
                describeTo: {description ->
                    description.appendText('An open task should have the status: ')
                            .appendValue(TaskStatus.OPEN)
                            .appendText(' and resolution: ')
                }
        ] as TypeSafeDiagnosingMatcher<TaskDTO>
    }

    def static wasClosedWithResolutionDone() {
        return [
                matchesSafely: {task, mismatchDescription ->
                    mismatchDescription.appendText('but had the closer: ')
                            .appendValue(task.closer)
                            .appendText(' and the status: ')
                            .appendValue(task.status)
                            .appendText(' and the resolution: ')
                            .appendValue(task.resolution)
                    (task.closer != null) && (task.status == TaskStatus.CLOSED) && (task.resolution == TaskResolution.DONE)
                },
                describeTo: {description ->
                    description.appendText('A closed task should have a closer and the status: ')
                            .appendValue(TaskStatus.CLOSED)
                            .appendText(' and the resolution: ')
                            .appendValue(TaskResolution.DONE)
                }
        ] as TypeSafeDiagnosingMatcher<TaskDTO>
    }
}
