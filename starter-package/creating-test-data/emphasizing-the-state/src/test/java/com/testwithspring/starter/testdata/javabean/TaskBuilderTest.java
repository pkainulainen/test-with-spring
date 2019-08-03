package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

/**
 * This class demonstrates how we can emphasize the state of the created
 * {@code Task} objects by using a test data builder.
 *
 * <strong>Note:</strong> The test method names found from this class suck. Do not use similar names in your tests.
 *
 * @author Petri Kainulainen
 */
public class TaskBuilderTest {

    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CLOSER_ID = 55L;

    /**
     * This test method requires an open task. The other properties of
     * the created {@code Task} object are irrelevant to this test method.
     */
    @Test
    public void createOpenTaskWithoutAssignee() {
        Task openTask = new TaskBuilder()
                .withStatusOpen()
                .build();
    }

    /**
     * This test method requires an open task that is assigned to the
     * specified assignee. The other properties of the created {@code Task}
     * object are irrelevant to this test method.
     */
    @Test
    public void createOpenTaskThatIsAssignedToAssignee() {
        Task assignedTask = new TaskBuilder()
                .withAssignee(ASSIGNEE_ID)
                .withStatusOpen()
                .build();
    }

    /**
     * This test method requires a task that is in progress. Also, it
     * is important the task is assigned to the specified person. The
     * other properties of the created {@code Task} object are irrelevant
     * to this test method.
     */
    @Test
    public void createTaskThatIsInProgress() {
        Task inProgress = new TaskBuilder()
                .withAssignee(ASSIGNEE_ID)
                .withStatusInProgress()
                .build();
    }

    /**
     * This test method requires a task that:
     * <ul>
     *     <li>is assigned to the specified person.</li>
     *     <li>was closed because it was done.</li>
     *     <li>was closed by the specified person.</li>
     * </ul>
     *
     * The other properties of the created {@code Task} object aren't important
     * to our test method.
     */
    @Test
    public void createTaskThatWasClosedAsDone() {
        Task done = new TaskBuilder()
                .withAssignee(ASSIGNEE_ID)
                .withResolutionDone(CLOSER_ID)
                .build();
    }

    /**
     * This test method requires a task that:
     * <ul>
     *     <li>is assigned to the specified person.</li>
     *     <li>was closed as a duplicate.</li>
     *     <li>was closed by the specified person.</li>
     * </ul>
     *
     * The other properties of the created {@code Task} object aren't important
     * to our test method.
     */
    @Test
    public void createTaskThatWasClosedAsDuplicate() {
        Task duplicate = new TaskBuilder()
                .withAssignee(ASSIGNEE_ID)
                .withResolutionDuplicate(CLOSER_ID)
                .build();
    }

    /**
     * This test method requires a task that:
     * <ul>
     *     <li>is assigned to the specified person.</li>
     *     <li>was closed because it won't be done.</li>
     *     <li>was closed by the specified person.</li>
     * </ul>
     *
     * The other properties of the created {@code Task} object aren't important
     * to our test method.
     */
    @Test
    public void createTaskThatWasClosedAsWontDo() {
        Task wontDo = new TaskBuilder()
                .withAssignee(ASSIGNEE_ID)
                .withResolutionWontDo(CLOSER_ID)
                .build();
    }
}