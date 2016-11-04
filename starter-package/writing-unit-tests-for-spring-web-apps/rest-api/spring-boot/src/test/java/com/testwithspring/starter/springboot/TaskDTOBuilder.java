package com.testwithspring.starter.springboot;

import com.testwithspring.starter.springboot.task.TaskDTO;
import com.testwithspring.starter.springboot.task.TaskResolution;
import com.testwithspring.starter.springboot.task.TaskStatus;

/**
 * You should consider using a builder because the information
 * of a {@code TaskDTO} object depends from the status of the
 * task. In other words, {@code TaskDTO} objects are not just dummy
 * data containers.
 */
public class TaskDTOBuilder {

    private Long assigneeId;
    private Long closerId;
    private Long creatorId;
    private String description;
    private Long id;
    private TaskResolution resolution;
    private TaskStatus status;
    private String title;

    public TaskDTOBuilder() {
    }

    public TaskDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TaskDTOBuilder withAssignee(Long assigneeId) {
        this.assigneeId = assigneeId;
        return this;
    }

    public TaskDTOBuilder withCloser(Long closerId) {
        this.closerId = closerId;
        return this;
    }

    public TaskDTOBuilder withCreator(Long creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public TaskDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskDTOBuilder withResolutionDuplicate(Long closerId) {
        this.closerId = closerId;
        this.resolution = TaskResolution.DUPLICATE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskDTOBuilder withResolutionDone(Long closerId) {
        this.closerId = closerId;
        this.resolution = TaskResolution.DONE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskDTOBuilder withResolutionWontDo(Long closerId) {
        this.closerId = closerId;
        this.resolution = TaskResolution.WONT_DO;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskDTOBuilder withStatusInProgress() {
        this.status = TaskStatus.IN_PROGRESS;
        return this;
    }

    public TaskDTOBuilder withStatusOpen() {
        this.status = TaskStatus.OPEN;
        return this;
    }


    public TaskDTOBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskDTO build() {
        TaskDTO dto = new TaskDTO();
        dto.setAssigneeId(this.assigneeId);
        dto.setCloserId(this.closerId);
        dto.setCreatorId(this.creatorId);
        dto.setDescription(this.description);
        dto.setId(this.id);
        dto.setResolution(this.resolution);
        dto.setStatus(this.status);
        dto.setTitle(this.title);
        return dto;
    }
}
