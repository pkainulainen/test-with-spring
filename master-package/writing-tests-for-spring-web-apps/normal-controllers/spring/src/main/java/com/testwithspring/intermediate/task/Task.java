package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.common.AbstractEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
class Task extends AbstractEntity {

    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    @Embedded
    private Assignee assignee;

    @Embedded
    private Closer closer;

    @Embedded
    private Creator creator;

    @Column(name = "description", length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Embedded
    private Modifier modifier;

    @Column(name = "resolution")
    @Enumerated(EnumType.STRING)
    private TaskResolution resolution;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToMany
    @JoinTable(
            name="tasks_tags",
            joinColumns=@JoinColumn(name="task_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="tag_id", referencedColumnName="id"))
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    /**
     * Required by Hibernate.
     */
    private Task() {}

    private Task(Builder builder) {
        this.creator = builder.creator;
        this.description = builder.description;

        if (builder.creator != null) {
            this.modifier = new Modifier(builder.creator.getUserId());
        }

        this.title = builder.title;
        this.status = TaskStatus.OPEN;
    }

    static Builder getBuilder() {
        return new Builder();
    }

    Assignee getAssignee() {
        return assignee;
    }

    Closer getCloser() {
        return closer;
    }

    Creator getCreator() {
        return creator;
    }

    String getDescription() {
        return description;
    }

    Modifier getModifier() {
        return modifier;
    }

    TaskResolution getResolution() {
        return resolution;
    }

    TaskStatus getStatus() {
        return status;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    String getTitle() {
        return title;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setModifier(Long modifierId) {
        this.modifier = new Modifier(modifierId);
    }

    void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("assignee", this.assignee)
                .append("closer", this.closer)
                .append("creationTime", this.getCreationTime())
                .append("creator", this.creator)
                .append("description", this.description)
                .append("modificationTime", this.getModificationTime())
                .append("modifier", this.modifier)
                .append("resolution", this.resolution)
                .append("status", this.status)
                .append("title", this.title)
                .append("version", this.getVersion())
                .build();
    }

    static class Builder {

        private Creator creator;
        private String description;
        private String title;

        private Builder() {}

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
            Task task = new Task(this);

            checkCreator(task);
            checkDescription(task);
            checkTitle(task);

            return task;
        }

        private void checkCreator(Task task) {
            Creator creator = task.getCreator();
            if (creator == null) {
                throw new NullPointerException("You cannot create a new task without specifying the creator of the task");
            }
        }

        private void checkDescription(Task task) {
            String description = task.getDescription();
            if (description != null) {
                if (description.length() > MAX_LENGTH_DESCRIPTION) {
                    throw new IllegalStateException(String.format(
                            "The maximum length of the description is: %d characters. Current description has %d characters",
                            MAX_LENGTH_DESCRIPTION,
                            description.length()
                    ));
                }
            }
        }

        private void checkTitle(Task task) {
            String title = task.getTitle();
            if (title == null) {
                throw new NullPointerException("You cannot create a new task without specifying the title of the task");
            }

            if (title.isEmpty()) {
                throw new IllegalStateException("You cannot create a new task without specifying a non-empty title");
            }

            if (title.length() > MAX_LENGTH_TITLE) {
                throw new IllegalStateException(String.format(
                        "The maximum length of the title is: %d characters. Current title has %d characters",
                        MAX_LENGTH_TITLE,
                        title.length()
                ));
            }
        }
    }
}
