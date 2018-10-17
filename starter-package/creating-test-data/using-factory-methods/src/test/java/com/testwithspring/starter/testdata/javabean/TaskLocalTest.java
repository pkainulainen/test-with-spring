package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

/**
 * This class demonstrates how we can create new {@code Task} objects
 * by using local factory methods. The goal of these examples is to
 * demonstrate the differences between factory methods and the JavaBeans
 * style construction.
 *
 * @author Petri Kainulainen
 */
public class TaskLocalTest {

    private static final Long ID = 1L;
    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CLOSER_ID = 55L;
    private static final Long CREATOR_ID = 44L;
    private static final Long MODIFIER_ID = 23L;
    private static final String TITLE = "Write an example project";
    private static final String DESCRIPTION = "Write an example project that demonstrates how we can use factory methods in our tests.";

    @Test
    public void createOpenTaskWithoutAssigneeWithLocalMethodWithoutParameters() {
        Task openTaskWithoutAssignee = createOpenTaskWithoutAssignee();
    }

    /**
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates an open task that
     * isn't assigned to anyone.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that this method creates
     *         an open task that isn't assigned to anyone. This works well if
     *         we care only about the fact that the created task is an open
     *         task that isn't assigned to anyone, and we aren't interested in
     *         the other field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
     */
    private Task createOpenTaskWithoutAssignee() {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(null);
        task.setCloser(null);
        task.setCreator(new Creator(CREATOR_ID));
        task.setModifier(new Modifier(MODIFIER_ID));
        task.setTitle(TITLE);
        task.setDescription(DESCRIPTION);
        task.setStatus(TaskStatus.OPEN);
        task.setResolution(null);
        return task;
    }


    @Test
    public void createOpenTaskWithoutAssigneeWithLocalMethodWithParameters() {
        Task openTaskWithoutAssignee = createOpenTaskWithoutAssignee(ID,
                CREATOR_ID,
                MODIFIER_ID,
                TITLE,
                DESCRIPTION
        );
    }

    /**
     * This factory method takes the information of the created task as a method
     * parameter and uses this information when it creates an open task that isn't
     * assigned to anyone.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It hides the fact that assignee must be null.</li>
     *     <li>It hides the logic that sets the status and resolution of an open task.</li>
     *     <li>It preserves the connection between our test method and test data.</li>
     * </ul>
     *
     * The cons of this factory method is:
     * <ul>
     *     <li>
     *         It has five method parameters and the consecutive method parameters have the same type.
     *         This means that it can be hard to remember the order of method parameters. Thus, I think
     *         that we shouldn't write factory methods that have so many method parameters.
     *     </li>
     * </ul>
     */
    private Task createOpenTaskWithoutAssignee(Long taskId,
                                               Long creatorId,
                                               Long modifierId,
                                               String title,
                                               String description) {
        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(null);
        task.setCloser(null);
        task.setCreator(new Creator(creatorId));
        task.setModifier(new Modifier(modifierId));
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates an open task that
     * is assigned to someone.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that this method creates
     *         an open task that is assigne to someone. This works well if
     *         we care only about the fact that the created task is an open
     *         task that is assigned to someone, and we aren't interested in
     *         the other field values of the created object (this includes the assignee).
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
     */
    private Task createOpenTaskThatIsAssignedToAssignee() {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(new Assignee(ASSIGNEE_ID));
        task.setCloser(null);
        task.setCreator(new Creator(CREATOR_ID));
        task.setModifier(new Modifier(MODIFIER_ID));
        task.setTitle(TITLE);
        task.setDescription(DESCRIPTION);
        task.setStatus(TaskStatus.OPEN);
        task.setResolution(null);
        return task;
    }

    @Test
    public void createOpenTaskWithAssigneeWithLocalMethod() {
        Task openTaskWithAssignee = createOpenTaskThatIsAssignedTo(ASSIGNEE_ID);
    }

    /**
     * This factory method has one method parameter that identifies the assignee
     * of the created open task.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that this method creates
     *         an open task that is assigned to the given person. This works well if
     *         we care only about the fact that the created task is an open
     *         task that is assigned to the given person, and we aren't interested in
     *         the other field values of the created object.
     *      </li>
     *      <li>
     *          It has only one method parameter that emphasizes the assignee of the
     *          created task.
     *      </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         It sets the "other" field values of the created object by using the
     *         constants declared by our test class. This breaks the connection between
     *         our test method and test data.
     *     </li>
     * </ul>
     */
    private Task createOpenTaskThatIsAssignedTo(Long assigneeId) {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(new Assignee(assigneeId));
        task.setCloser(null);
        task.setCreator(new Creator(CREATOR_ID));
        task.setModifier(new Modifier(MODIFIER_ID));
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
                MODIFIER_ID,
                TITLE,
                DESCRIPTION
        );
    }

    /**
     * This factory method takes the information of the created task as a method
     * parameter and uses this information when it creates an open task that is
     * assigned to a person.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It hides the logic that sets the status and resolution of an open task.</li>
     *     <li>It preserves the connection between our test method and test data.</li>
     * </ul>
     *
     * The cons of this factory method is:
     * <ul>
     *     <li>
     *         It has six method parameters and the consecutive method parameters have the same type.
     *         This means that it can be hard to remember the order of method parameters. Thus, I think
     *         that we shouldn't write factory methods that have so many method parameters.
     *     </li>
     * </ul>
     */
    private Task createOpenTaskThatIsAssignedToAssignee(Long taskId,
                                                        Long assigneeId,
                                                        Long creatorId,
                                                        Long modifierId,
                                                        String title,
                                                        String description) {
        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(new Assignee(assigneeId));
        task.setCloser(null);
        task.setCreator(new Creator(creatorId));
        task.setModifier(new Modifier(modifierId));
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates a task that was closed
     * as a duplicate.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that this method creates
     *         a task that was closed as a duplicate. This works well if
     *         we care only about the fact that the created task was closed
     *         as a duplicate , and we aren't interested in the other field values
     *         of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
     */
    private Task createTaskThatWasClosedAsDuplicate() {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(new Assignee(ASSIGNEE_ID));
        task.setCloser(new Closer(CLOSER_ID));
        task.setCreator(new Creator(CREATOR_ID));
        task.setModifier(new Modifier(MODIFIER_ID));
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
                MODIFIER_ID,
                TITLE,
                DESCRIPTION
        );
    }

    /**
     * This factory method takes the information of the created task as a method
     * parameter and uses this information when it creates a task that was closed
     * as a duplicate.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It hides the logic that sets the status and resolution of the created task.</li>
     *     <li>It preserves the connection between our test method and test data.</li>
     * </ul>
     *
     * The cons of this factory method is:
     * <ul>
     *     <li>
     *         It has seven method parameters and the consecutive method parameters have the same type.
     *         This means that it can be hard to remember the order of method parameters. Thus, I think
     *         that we shouldn't write factory methods that have so many method parameters.
     *     </li>
     * </ul>
     */
    private Task createTaskThatWasClosedAsDuplicate(Long taskId,
                                                    Long assigneeId,
                                                    Long creatorId,
                                                    Long closerId,
                                                    Long modifierId,
                                                    String title,
                                                    String description) {
        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(new Assignee(assigneeId));
        task.setCloser(new Closer(closerId));
        task.setCreator(new Creator(creatorId));
        task.setModifier(new Modifier(modifierId));
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.CLOSED);
        task.setResolution(TaskResolution.DUPLICATE);
        return task;
    }

    @Test
    public void createClosedTaskWithCloserAndResolution() {
        Task closedAsDuplicate = createClosedTask(CLOSER_ID, TaskResolution.DUPLICATE);
    }

    /**
     * This factory method has two method parameters that identify the closer
     * and resolution of the closed task.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that this method creates
     *         a closed task.
     *     </li>
     *     <li>
     *          It has only two method parameters which emphasize that the created
     *          task was closed by a specific person who used the resolution given
     *          as a method parameter. This works well if we care only about the
     *          fact that the created task was closed the given person who used
     *          the given resolution, and we don't care about the other field values
     *          of the created object.
     *      </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         It sets the "other" field values of the created object by using the
     *         constants declared by our test class. This breaks the connection between
     *         our test method and test data.
     *     </li>
     * </ul>
     */
    private Task createClosedTask(Long closerId, TaskResolution resolution) {
        Task task = new Task();
        task.setId(ID);
        task.setAssignee(new Assignee(ASSIGNEE_ID));
        task.setCloser(new Closer(closerId));
        task.setCreator(new Creator(CREATOR_ID));
        task.setModifier(new Modifier(MODIFIER_ID));
        task.setTitle(TITLE);
        task.setDescription(DESCRIPTION);
        task.setStatus(TaskStatus.CLOSED);
        task.setResolution(resolution);
        return task;
    }
}
