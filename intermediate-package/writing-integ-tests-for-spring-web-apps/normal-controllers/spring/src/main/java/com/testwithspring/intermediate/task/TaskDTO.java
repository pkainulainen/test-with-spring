package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.user.PersonDTO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDTO {

    private Long assigneeId;
    private Long closerId;
    private ZonedDateTime creationTime;
    private PersonDTO creator;
    private String description;
    private Long id;
    private ZonedDateTime modificationTime;
    private TaskResolution resolution;
    private TaskStatus status;
    private List<TagDTO> tags = new ArrayList<>();
    private String title;

    public TaskDTO() {}

    public Long getAssigneeId() {
        return assigneeId;
    }

    public Long getCloserId() {
        return closerId;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public PersonDTO getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getModificationTime() {
        return modificationTime;
    }

    public TaskResolution getResolution() {
        return resolution;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public List<TagDTO> getTags() {
        return tags;
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

    public void setCreationTime(ZonedDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public void setCreator(PersonDTO creator) {
        this.creator = creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setModificationTime(ZonedDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public void setResolution(TaskResolution resolution) {
        this.resolution = resolution;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
