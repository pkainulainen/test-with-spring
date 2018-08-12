package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

/**
 * This class demonstrates the problem we face when we creates instances of a class
 * that follows the JavaBeans pattern.
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
    private static final Long MODIFIER_ID = 66L;
    private static final String TITLE = "Write an example project";
    private static final String DESCRIPTION = "description";

    /**
     * The problem is that we have to know that:
     * <ul>
     *     <li>An open task is a task that has the status: <code>TaskStatus.OPEN</code>.</li>
     *     <li>An open task has no resolution.</li>
     *     <li>The creator and modifier of the task must be specified.</li>
     *     <li>The assignee and closer must be null.</li>
     * </ul>
     */
    @Test
    public void createOpenTaskWithoutAssignee() {
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
    }

    /**
     * The problem is that we have to know that:
     * <ul>
     *     <li>An open task is a task that has the status: <code>TaskStatus.OPEN</code>.</li>
     *     <li>An open task has no resolution.</li>
     *     <li>The assignee, creator, and modifier of the task must be specified.</li>
     *     <li>The closer of the task must be null.</li>
     * </ul>
     */
    @Test
    public void createOpenTaskThatIsAssignedToAssignee() {
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
    }

    /**
     * The problem is that we have to know that:
     * <ul>
     *     <li>A closed task is a task that has the status: <code>TaskStatus.CLOSED</code>.</li>
     *     <li>Because the task was closed as a duplicate, its resolution must be: <code>TaskResolution.DUPLICATE</code>.</li>
     *     <li>The assignee, creator, modifier, and closer of the task must be specified.</li>
     * </ul>
     */
    @Test
    public void createTaskThatWasClosedAsDuplicate() {
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
    }
}
