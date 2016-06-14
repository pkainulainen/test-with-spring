package com.testwithspring.starter.assertions.task;

import java.lang.reflect.Field;

/**
 * This test data builder demonstrates how we can override the builder provided
 * by the {@code Task} class.
 */
public final class TaskBuilder {

    private static final Long NOT_IN_USE = -1L;
    private static final String NOT_IMPORTANT = "NOT IMPORTANT";

    private Long assigneeId;
    private Long closerId;
    private Long creatorId = NOT_IN_USE;
    private String description;
    private Long id;
    private TaskResolution resolution;
    private TaskStatus status;
    private String title = NOT_IMPORTANT;

    public TaskBuilder() {
    }

    public static Task openTaskWithoutAssignee() {
        return new TaskBuilder()
                .withStatusOpen()
                .build();
    }

    public static Task openTaskWithAssignee(Long assigneeId) {
        return new TaskBuilder()
                .withAssignee(assigneeId)
                .withStatusOpen()
                .build();
    }

    public static Task taskThatIsInProgress(Long assigneeId) {
        return new TaskBuilder()
                .withAssignee(assigneeId)
                .withStatusInProgress()
                .build();
    }

    public static Task closedAsDone(Long closerId) {
        return new TaskBuilder()
                .withAssignee(NOT_IN_USE)
                .withResolutionDone(closerId)
                .build();
    }

    public static Task closedAsDuplicate(Long closerId) {
        return new TaskBuilder()
                .withAssignee(NOT_IN_USE)
                .withResolutionDuplicate(closerId)
                .build();
    }

    public static Task closedAsWontDo(Long closerId) {
        return  new TaskBuilder()
                .withAssignee(NOT_IN_USE)
                .withResolutionWontDo(closerId)
                .build();
    }

    public TaskBuilder withAssignee(Long assigneeId) {
        this.assigneeId = assigneeId;
        return this;
    }

    public TaskBuilder withCreator(Long creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public TaskBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TaskBuilder withResolutionDuplicate(Long closerId) {
        this.closerId = closerId;
        this.resolution = TaskResolution.DUPLICATE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskBuilder withResolutionDone(Long closerId) {
        this.closerId = closerId;
        this.resolution = TaskResolution.DONE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskBuilder withResolutionWontDo(Long closerId) {
        this.closerId = closerId;
        this.resolution = TaskResolution.WONT_DO;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskBuilder withStatusInProgress() {
        this.status = TaskStatus.IN_PROGRESS;
        return this;
    }

    public TaskBuilder withStatusOpen() {
        this.status = TaskStatus.OPEN;
        return this;
    }

    public TaskBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public Task build() {
        Task task = Task.getBuilder()
                .withAssignee(assigneeId)
                .withCreator(creatorId)
                .withDescription(description)
                .withId(id)
                .withTitle(title)
                .build();

        if (closerId != null) {
            setFieldValue(task, "closer", new Closer(closerId));
        }

        setFieldValue(task, "status", this.status);
        setFieldValue(task, "resolution", this.resolution);

        return task;
    }

    private void setFieldValue(Task target, String fieldName, Object fieldValue) {
        try {
            Field targetField = target.getClass().getDeclaredField(fieldName);
            targetField.setAccessible(true);
            targetField.set(target, fieldValue);
        }
        catch (Exception ex) {
            throw new RuntimeException(
                    String.format("Cannot set the value of the field: %s because of an error", fieldName),
                    ex
            );
        }
    }
}
