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
     * connection of the test method and the test data.
     *
     * That being said, if we only care about the fact that the task is open and it
     * has no assignee, and the other property values are irrelevant to us, using this
     * factory method is a good choice.
     */
    public static Task createOpenTaskWithoutAssignee() {
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

    /**
     * Even though this factory method helps us to hide the state and resolution of
     * an open task, the problem is that the method still has four method parameters.
     * Nevertheless, it might make sense to use this if the method is used only by a few
     * test classes and we don't already a test data builder.
     */
    public static Task createOpenTaskWithoutAssignee(Long taskId,
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

    /**
     * Using a factory method like this is a horrible idea because it breaks the
     * connection of the test method and the test data.
     *
     * That being said, if we only care about the fact that the task is open and it
     * has an assignee, and the other property values are irrelevant to us, using this
     * factory method is a good choice.
     */
    public static Task createOpenTaskThatIsAssignedToAssignee() {
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

    /**
     * Even though this factory method helps us to hide the state and resolution of
     * an open task, the problem is that the method still has five method parameters that
     * is too much for my taste.
     */
    public static Task createOpenTaskThatIsAssignedToAssignee(Long taskId,
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

    /**
     * Using a factory method like this is a horrible idea because it breaks the
     * connection of the test method and the test data.
     *
     * That being said, if we only care about the fact that the task was closed as
     * a duplicate, and the other property values are irrelevant to us, using this
     * factory method is a good choice.
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
