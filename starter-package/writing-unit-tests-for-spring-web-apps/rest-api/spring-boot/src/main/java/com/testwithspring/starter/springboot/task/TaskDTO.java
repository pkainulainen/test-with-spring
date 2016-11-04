package com.testwithspring.starter.springboot.task;

public class TaskDTO {

    private Long assigneeId;
    private Long closerId;
    private Long creatorId;
    private String description;
    private Long id;
    private TaskResolution resolution;
    private TaskStatus status;
    private String title;

    public TaskDTO() {}

    public Long getAssigneeId() {
        return assigneeId;
    }

    public Long getCloserId() {
        return closerId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public TaskResolution getResolution() {
        return resolution;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public void setCloserId(Long closerId) {
        this.closerId = closerId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setResolution(TaskResolution resolution) {
        this.resolution = resolution;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
