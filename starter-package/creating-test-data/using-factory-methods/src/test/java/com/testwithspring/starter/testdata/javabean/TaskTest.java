package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

/**
 * This class demonstrates how we can create new {@code Task} objects by using factory methods.
 *
 * <strong>Note:</strong> The test method names found from this class suck. Do not use similar names in your tests.
 *
 * @author Petri Kainulainen
 */
public class TaskTest {

    private static final Long ID = 1L;
    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CLOSER_ID = 55L;
    private static final Long CREATOR_ID = 44L;
    private static final String TITLE = "Write an example project";
    private static final String DESCRIPTION = "Write an example project that demonstrates how we can use factory methods in our tests.";

    @Test
    public void createOpenTaskWithoutAssigneeWithLocalMethodWithoutParameters() {
        Task openTaskWithoutAssignee = createOpenTaskWithoutAssignee();
    }

    /**
     * The problem is that this method breaks the connection of the test method
     * and test data.
     *
     * That being said, if we only care about the fact that the task is open and it
     * has no assignee, and the other property values are irrelevant to us, using this
     * factory method is a good choice.
     */
    private Task createOpenTaskWithoutAssignee() {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(null);
        task.setCloser(null);
        task.setCreator(new Creator(CREATOR_ID));
        task.setTitle(TITLE);
        task.setDescription(DESCRIPTION);
        task.setStatus(TaskStatus.OPEN);
        task.setResolution(null);
        return task;
    }


    @Test
    public void createOpenTaskWithoutAssigneeWithLocalMethodWithParameters() {
        Task openTaskWithoutAssignee = createOpenTaskWithoutAssignee(ID,
                ASSIGNEE_ID,
                TITLE,
                DESCRIPTION
        );
    }

    /**
     * Even though this factory method helps us to hide the state and resolution of
     * an open task, the problem is that the method still has four method parameters.
     * Nevertheless, it might make sense to use this if the method is used only by
     * this test class and we don't already a test data builder.
     */
    private Task createOpenTaskWithoutAssignee(Long taskId,
                                                     Long creatorId,
                                                     String title,
                                                     String description) {
        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(null);
        task.setCloser(null);
        task.setCreator(new Creator(creatorId));
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.OPEN);
        task.setResolution(null);
        return task;
    }

    @Test
    public void createOpenTaskWithAssigneeWithLocalMethodWithoutParameters() {
        Task openTaskWithAssignee = createOpenTaskThatIsAssignedToAssignee();
    }

    /**
     * The problem is that this method breaks the connection of the test method
     * and test data.
     *
     * That being said, if we only care about the fact that the task is open and it
     * has an assignee, and the other property values are irrelevant to us, using this
     * factory method is a good choice.
     */
    private Task createOpenTaskThatIsAssignedToAssignee() {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(new Assignee(ASSIGNEE_ID));
        task.setCloser(null);
        task.setCreator(new Creator(CREATOR_ID));
        task.setTitle(TITLE);
        task.setDescription(DESCRIPTION);
        task.setStatus(TaskStatus.OPEN);
        task.setResolution(null);

        return task;
    }

    @Test
    public void createOpenTaskWithAssigneeWithLocalMethodWithParameters() {
        Task openTaskWithAssignee = createOpenTaskThatIsAssignedToAssignee(ID,
                ASSIGNEE_ID,
                CREATOR_ID,
                TITLE,
                DESCRIPTION
        );
    }

    /**
     * Even though this factory method helps us to hide the state and resolution of
     * an open task, the problem is that the method still has five method parameters that
     * is too much for my taste.
     */
    private Task createOpenTaskThatIsAssignedToAssignee(Long taskId,
                                                              Long assigneeId,
                                                              Long creatorId,
                                                              String title,
                                                              String description) {
        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(new Assignee(assigneeId));
        task.setCloser(null);
        task.setCreator(new Creator(creatorId));
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.OPEN);
        task.setResolution(null);
        return task;
    }

    @Test
    public void createTaskThatWasClosedAsDuplicateWithLocalMethodWithoutParameters() {
        Task closedAsDuplicate = createTaskThatWasClosedAsDuplicate();
    }

    /**
     * The problem is that this method breaks the connection of the test method
     * and test data.
     *
     * That being said, if we only care about the fact that the task was closed as
     * a duplicate, and the other property values are irrelevant to us, using this
     * factory method is a good choice.
     */
    private Task createTaskThatWasClosedAsDuplicate() {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(new Assignee(ASSIGNEE_ID));
        task.setCloser(new Closer(CLOSER_ID));
        task.setCreator(new Creator(CREATOR_ID));
        task.setTitle(TITLE);
        task.setDescription(DESCRIPTION);
        task.setStatus(TaskStatus.CLOSED);
        task.setResolution(TaskResolution.DUPLICATE);
        return task;
    }

    @Test
    public void createTaskThatWasClosedAsDuplicateWithLocalMethodWithParameters() {
        Task closedAsDuplicate = createTaskThatWasClosedAsDuplicate(ID,
                ASSIGNEE_ID,
                CREATOR_ID,
                CLOSER_ID,
                TITLE,
                DESCRIPTION
        );
    }

    /**
     * Even though this factory method helps us to hide the state and resolution of
     * a closed task, the problem is that the method still has six method parameters that
     * is too much for my taste.
     */
    private Task createTaskThatWasClosedAsDuplicate(Long taskId,
                                                          Long assigneeId,
                                                          Long creatorId,
                                                          Long closerId,
                                                          String title,
                                                          String description) {
        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(new Assignee(assigneeId));
        task.setCloser(new Closer(closerId));
        task.setCreator(new Creator(creatorId));
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.CLOSED);
        task.setResolution(TaskResolution.DUPLICATE);
        return task;
    }
}
