package com.testwithspring.starter.assertions.task;

public class Task {

    private Long id;
    private Assignee assignee;
    private Closer closer;
    private Creator creator;
    private String description;
    private String title;
    private TaskStatus status;
    private TaskResolution resolution;

    private Task(Builder builder) {
        this.id = builder.id;
        this.assignee = builder.assignee;
        this.closer = null;
        this.creator = builder.creator;
        this.description = builder.description;
        this.title = builder.title;
        this.status = TaskStatus.OPEN;
        this.resolution = null;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public Closer getCloser() {
        return closer;
    }

    public Creator getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskResolution getResolution() {
        return resolution;
    }

    public static class Builder {

        private Long id;
        private Assignee assignee;
        private Creator creator;
        private String description;
        private String title;

        private Builder() {}

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withAssignee(Long assigneeId) {
            if (assigneeId != null) {
                this.assignee = new Assignee(assigneeId);
            }
            return this;
        }

        public Builder withCreator(Long creatorId) {
            if (creatorId != null) {
                this.creator = new Creator(creatorId);
            }
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Task build () {
            return new Task(this);
        }
    }
}
