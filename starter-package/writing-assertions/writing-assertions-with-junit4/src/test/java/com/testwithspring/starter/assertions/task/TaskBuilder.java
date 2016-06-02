package com.testwithspring.starter.assertions.task;

import java.lang.reflect.Field;

/**
 * This test data builder demonstrates how we can override the builder provided
 * by the {@code Task} class.
 */
public final class TaskBuilder {

    private Long assigneeId;
    private Long closerId;
    private Long creatorId;
    private String description;
    private Long id;
    private TaskResolution resolution;
    private TaskStatus status;
    private String title;

    public TaskBuilder() {

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
