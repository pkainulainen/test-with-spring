package com.testwithspring.starter.testdata.javabean;

import org.junit.Test;

/**
 * This class demonstrates how we can create new {@code Task} objects by a test data builder.
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
    private static final String DESCRIPTION = "Write an example project that demonstrates how we can use factory methods in our tests.";

    /**
     * Here we have to know that:
     * <ul>
     *     <li>The assignee must not be set.</li>
     *     <li>The creator must be set.</li>
     *     <li>The modifier must be set.</li>
     *     <li>The closer must not be set.</li>
     * </ul>
     */
    @Test
    public void createOpenTaskWithoutAssignee() {
        Task openTask = new TaskBuilder()
                .withId(ID)
                .withCreator(CREATOR_ID)
                .withModifier(MODIFIER_ID)
                .withStatusOpen()
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();
    }

    /**
     * Here we have to know that:
     * <ul>
     *     <li>The assignee must be set.</li>
     *     <li>The creator must be set.</li>
     *     <li>The modifier must be set.</li>
     *     <li>The closer must not be set.</li>
     * </ul>
     */
    @Test
    public void createOpenTaskThatIsAssignedToAssignee() {
        Task assignedTask = new TaskBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withModifier(MODIFIER_ID)
                .withStatusOpen()
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();
    }

    /**
     * Here we have to know that:
     * <ul>
     *     <li>The assignee must be set.</li>
     *     <li>The creator must be set.</li>
     *     <li>The modifier must be set.</li>
     *     <li>The closer must not be set.</li>
     * </ul>
     */
    @Test
    public void createTaskThatIsInProgress() {
        Task task = new TaskBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withModifier(MODIFIER_ID)
                .withStatusInProgress()
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .build();
    }

    /**
     * Here we have to know that:
     * <ul>
     *     <li>The assignee must be set.</li>
     *     <li>The creator must be set.</li>
     *     <li>The modifier must be set.</li>
     *     <li>A closed task must have a closer.</li>
     * </ul>
     */
    @Test
    public void createTaskThatWasClosedAsDone() {
        Task duplicate = new TaskBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withModifier(MODIFIER_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .withResolutionDone(CLOSER_ID)
                .build();
    }

    /**
     * Here we have to know that:
     * <ul>
     *     <li>The assignee must be set.</li>
     *     <li>The creator must be set.</li>
     *     <li>The modifier must be set.</li>
     *     <li>A closed task must have a closer.</li>
     * </ul>
     */
    @Test
    public void createTaskThatWasClosedAsDuplicate() {
        Task duplicate = new TaskBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withModifier(MODIFIER_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .withResolutionDuplicate(CLOSER_ID)
                .build();
    }

    /**
     * Here we have to know that:
     * <ul>
     *     <li>The assignee must be set.</li>
     *     <li>The creator must be set.</li>
     *     <li>The modifier must be set.</li>
     *     <li>A closed task must have a closer.</li>
     * </ul>
     */
    @Test
    public void createTaskThatWasClosedAsWontDo() {
        Task duplicate = new TaskBuilder()
                .withId(ID)
                .withAssignee(ASSIGNEE_ID)
                .withCreator(CREATOR_ID)
                .withModifier(MODIFIER_ID)
                .withTitle(TITLE)
                .withDescription(DESCRIPTION)
                .withResolutionWontDo(CLOSER_ID)
                .build();
    }
}