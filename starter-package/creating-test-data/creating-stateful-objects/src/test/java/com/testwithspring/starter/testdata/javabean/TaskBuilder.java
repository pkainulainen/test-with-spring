package com.testwithspring.starter.testdata.javabean;

public final class TaskBuilder {

    private Assignee assignee;
    private Closer closer;
    private Creator creator;
    private String description;
    private Long id;
    private Modifier modifier;
    private TaskResolution resolution;
    private TaskStatus status;
    private String title;

    public TaskBuilder() {

    }

    public TaskBuilder withAssignee(Long assigneeId) {
        this.assignee = new Assignee(assigneeId);
        return this;
    }

    public TaskBuilder withCreator(Long creatorId) {
        this.creator = new Creator(creatorId);
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

    public TaskBuilder withModifier(Long modifierId) {
        this.modifier = new Modifier(modifierId);
        return this;
    }

    public TaskBuilder withResolutionDuplicate(Long closerId) {
        this.closer = new Closer(closerId);
        this.resolution = TaskResolution.DUPLICATE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskBuilder withResolutionDone(Long closerId) {
        this.closer = new Closer(closerId);
        this.resolution = TaskResolution.DONE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskBuilder withResolutionWontDo(Long closerId) {
        this.closer = new Closer(closerId);
        this.resolution = TaskResolution.WONT_DO;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskBuilder withStatusInProgress() {
        this.closer = null;
        this.resolution = null;
        this.status = TaskStatus.IN_PROGRESS;
        return this;
    }

    public TaskBuilder withStatusOpen() {
        this.closer = null;
        this.resolution = null;
        this.status = TaskStatus.OPEN;
        return this;
    }

    public TaskBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public Task build() {
        Task task = new Task();

        task.setAssignee(assignee);
        task.setCloser(closer);
        task.setCreator(creator);
        task.setDescription(description);
        task.setId(id);
        task.setModifier(modifier);
        task.setResolution(resolution);
        task.setStatus(status);
        task.setTitle(title);

        return task;
    }
}
