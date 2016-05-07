package com.testwithspring.starter.testdata.javabean;

/**
 * This class demonstrates how we can create a so called object mother class that is used for creating
 * objects that are required by our tests.
 *
 * @author Petri Kainulainen
 */
public final class TaskFactory {

    private static final Long ID = 1L;
    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CLOSER_ID = 55L;
    private static final Long CREATOR_ID = 44L;
    private static final String TITLE = "Write an example project";
    private static final String DESCRIPTION = "Write an example project that demonstrates why using the new keyword is a bad idea.";

    /**
     * Prevents instantiation.
     */
    private TaskFactory() {}

    /**
     * Using a factory method like this is a horrible idea because it breaks the
     * connection of the test method and the test data. The worst part is that this
     * forces us to move the test data into the object mother class that shouldn't be aware of it.
     */
    public static Task createTaskThatWasClosedAsDuplicate() {
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
    public static Task createTaskThatWasClosedAsDuplicate(Long taskId,
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
