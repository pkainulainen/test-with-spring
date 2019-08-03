package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

/**
 * This class demonstrates how we can emphasize the state of the created
 * {@code Task} objects by an object mother class.
 *
 * <strong>Note:</strong> The test method names found from this class suck. Do not use similar names in your tests.
 *
 * @author Petri Kainulainen
 */
public class TaskObjectMotherTest {

    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CLOSER_ID = 55L;

    /**
     * This test method requires an open task that's not
     * assigned to anyone. The other properties of the task
     * are irrelevant to it.
     */
    @Test
    public void createOpenTaskWithoutAssignee() {
        Task openTask = TaskBuilder.openTaskWithoutAssignee();
    }

    /**
     * This test method requires an open task that's assigned to the
     * specified person. The other properties of the task are irrelevant
     * to it.
     */
    @Test
    public void createOpenTaskThatIsAssignedToAssignee() {
        Task assignedTask = TaskBuilder.openTaskWithAssignee(ASSIGNEE_ID);
    }

    /**
     * This test method requires a task that is in progress. Also, it
     * is important the task is assigned to the specified person. The
     * other properties of the created task are irrelevant to it.
     */
    @Test
    public void createTaskThatIsInProgress() {
        Task inProgress = TaskBuilder.taskThatIsInProgress(ASSIGNEE_ID);
    }

    /**
     * This test method requires a task that was closed because it was finished.
     * Also, it's important that the task was closed by the specified person.
     * The other properties of the created task are irrelevant to it.
     */
    @Test
    public void createTaskThatWasClosedAsDone() {
        Task done = TaskBuilder.closedAsDone(CLOSER_ID);
    }

    /**
     * This test method requires a task that was closed as a duplicate.
     * Also, it's important that the task was closed by the specified person.
     * The other properties of the created task are irrelevant to it.
     */
    @Test
    public void createTaskThatWasClosedAsDuplicate() {
        Task duplicate = TaskBuilder.closedAsDuplicate(CLOSER_ID);
    }

    /**
     * This test method requires a task that was closed because it won't be done.
     * Also, it's important that the task was closed by the specified person.
     * The other properties of the created task are irrelevant to it.
     */
    @Test
    public void createTaskThatWasClosedAsWontDo() {
        Task wontDo = TaskBuilder.closedAsWontDo(CLOSER_ID);
    }
}