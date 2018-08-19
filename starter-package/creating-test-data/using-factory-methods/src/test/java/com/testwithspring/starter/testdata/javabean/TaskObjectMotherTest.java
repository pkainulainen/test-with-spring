package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

/**
 * This test class demonstrates how we can create new {@code Task} objects
 * by using an object mother class. The goal of these examples is to demonstrate the
 * differences of factory methods and the JavaBeans style construction.
 */
public class TaskObjectMotherTest {

    private static final Long ID = 1L;
    private static final Long ASSIGNEE_ID = 99L;
    private static final Long CLOSER_ID = 55L;
    private static final Long CREATOR_ID = 44L;
    private static final Long MODIFIER_ID = 23L;
    private static final String TITLE = "Write an example project";
    private static final String DESCRIPTION = "Write an example project that demonstrates why using the new keyword is a bad idea.";


    @Test
    public void createOpenTaskWithoutAssigneeWithObjectMotherMethodWithoutParameters() {
        Task openTaskWithoutAssignee = TaskFactory.createOpenTaskWithoutAssignee();
    }

    @Test
    public void createOpenTaskWithoutAssigneeWithObjectMotherMethodWithParameters() {
        Task openTaskWithoutAssignee = TaskFactory.createOpenTaskWithoutAssignee(ID,
                CREATOR_ID,
                MODIFIER_ID,
                TITLE,
                DESCRIPTION
        );
    }

    @Test
    public void createOpenTaskWithAssigneeWithObjectMotherMethodWithoutParameters() {
        Task openTaskWithAssignee = TaskFactory.createOpenTaskThatIsAssignedToAssignee();
    }

    @Test
    public void createOpenTaskWithAssigneeWithObjectMotherMethod() {
        Task openTaskWithAssignee = TaskFactory.createOpenTaskThatIsAssignedTo(ASSIGNEE_ID);
    }

    @Test
    public void createOpenTaskWithAssigneeWithObjectMotherMethodWithParameters() {
        Task openTaskWithAssignee = TaskFactory.createOpenTaskThatIsAssignedToAssignee(ID,
                ASSIGNEE_ID,
                CREATOR_ID,
                MODIFIER_ID,
                TITLE,
                DESCRIPTION
        );
    }

    @Test
    public void createTaskThatWasClosedAsDuplicateWithObjectMotherMethodWithoutParameters() {
        Task closedAsDuplicate = TaskFactory.createTaskThatWasClosedAsDuplicate();
    }

    @Test
    public void createTaskThatWasClosedAsDuplicateWithObjectMotherMethodWithParameters() {
        Task closedAsDuplicate = TaskFactory.createTaskThatWasClosedAsDuplicate(ID,
                ASSIGNEE_ID,
                CREATOR_ID,
                CLOSER_ID,
                MODIFIER_ID,
                TITLE,
                DESCRIPTION
        );
    }

    @Test
    public void createClosedTaskWithCloserAndResolutionByUsingObjectMotherMethodWithParameters() {
        Task closedAsDuplicate = TaskFactory.createClosedTask(CLOSER_ID, TaskResolution.DONE);
    }
}
