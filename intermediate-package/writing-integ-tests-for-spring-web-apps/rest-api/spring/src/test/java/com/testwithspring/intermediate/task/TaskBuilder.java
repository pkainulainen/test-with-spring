package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.ReflectionFieldUtil;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class TaskBuilder {

    private static final Long NOT_IN_USE = -1L;
    private static final String NOT_IMPORTANT = "NOT_IMPORTANT";
    private static final ZonedDateTime NOT_SET = ZonedDateTime.now().minusYears(9999);

    private Long assigneeId = null;
    private Long closerId = null;
    private ZonedDateTime creationTime = NOT_SET;
    private Long creatorId = NOT_IN_USE;
    private String description = NOT_IMPORTANT;
    private Long id = NOT_IN_USE;
    private ZonedDateTime modificationTime = NOT_SET;
    private Long modifierId = NOT_IN_USE;
    private TaskResolution resolution = null;
    private TaskStatus status = null;
    private Set<Tag> tags = new HashSet<>();
    private String title = NOT_IMPORTANT;

    public TaskBuilder() {
    }

    public TaskBuilder withAssignee(Long assigneeId) {
        this.assigneeId = assigneeId;
        return this;
    }

    public TaskBuilder withCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
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

    public TaskBuilder withModificationTime(ZonedDateTime modificationTime) {
        this.modificationTime = modificationTime;
        return this;
    }

    public TaskBuilder withModifier(Long modifierId) {
        this.modifierId = modifierId;
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

    public TaskBuilder withTags(Tag... tags) {
        this.tags = new HashSet<>(Arrays.asList(tags));
        return this;
    }

    public TaskBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public Task build() {
        Task task = Task.getBuilder()
                .withCreator(creatorId)
                .withDescription(description)
                .withTitle(title)
                .build();

        if (assigneeId != null) {
            ReflectionFieldUtil.setFieldValue(task, "assignee", new Assignee(assigneeId));
        }
        if (closerId != null) {
            ReflectionFieldUtil.setFieldValue(task, "closer", new Closer(closerId));
        }

        ReflectionFieldUtil.setFieldValue(task, "creationTime", creationTime);
        ReflectionFieldUtil.setFieldValue(task, "id", id);
        ReflectionFieldUtil.setFieldValue(task, "modificationTime", modificationTime);

        if (modifierId != null) {
            ReflectionFieldUtil.setFieldValue(task, "modifier", new Modifier(modifierId));
        }

        ReflectionFieldUtil.setFieldValue(task, "status", status);
        ReflectionFieldUtil.setFieldValue(task, "resolution", resolution);
        ReflectionFieldUtil.setFieldValue(task, "tags", tags);

        return task;
    }
}
