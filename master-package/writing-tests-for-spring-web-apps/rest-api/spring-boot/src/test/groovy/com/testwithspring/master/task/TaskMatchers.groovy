package com.testwithspring.master.task

import org.hamcrest.TypeSafeDiagnosingMatcher

/**
 * Provides custom Hamcrest matchers that are used to write assertions
 * for {@link Task} objects.
 */
final class TaskMatchers {

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
        ] as TypeSafeDiagnosingMatcher<Task>
    }
}
