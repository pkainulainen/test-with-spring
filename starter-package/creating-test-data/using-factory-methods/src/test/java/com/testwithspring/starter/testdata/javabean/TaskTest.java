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
    public void createTaskThatWasClosedAsDuplicateWithLocalMethodWithoutParameters() {
        Task duplicateTask = createTaskThatWasClosedAsDuplicate();
    }

    @Test
    public void createTaskThatWasClosedAsDuplicateWithLocalMethodWithParameters() {
        Task duplicateTask = createTaskThatWasClosedAsDuplicate(ID,
                CREATOR_ID,
                CLOSER_ID,
                ASSIGNEE_ID,
                TITLE,
                DESCRIPTION
        );
    }

    @Test
    public void createTaskThatWasClosedAsDuplicateWithObjectMotherMethodWithoutParameters() {
        Task duplicateTask = TaskFactory.createTaskThatWasClosedAsDuplicate();
    }

    @Test
    public void createTaskThatWasClosedAsDuplicateWithObjectMotherMethodWithParameters() {
        Task duplicateTask = TaskFactory.createTaskThatWasClosedAsDuplicate(ID,
                CREATOR_ID,
                CLOSER_ID,
                ASSIGNEE_ID,
                TITLE,
                DESCRIPTION
        );
    }

    /**
     * This method takes no method parameters, but it has a price: using this
     * factory method breaks the connection between our test method and test data.
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

    /**
     * Even though this factory method helps us to hide the state and resolution of
     * a closed task, the problem is that the method still has six method parameters that
     * is too much for my taste.
     */
    private Task createTaskThatWasClosedAsDuplicate(Long taskId,
                                                    Long creatorId,
                                                    Long closerId,
                                                    Long assigneeId,
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
