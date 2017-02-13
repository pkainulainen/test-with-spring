package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.user.PersonDTO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDTO {

    private PersonDTO assignee;
    private PersonDTO closer;
    private ZonedDateTime creationTime;
    private PersonDTO creator;
    private String description;
    private Long id;
    private ZonedDateTime modificationTime;
    private PersonDTO modifier;
    private TaskResolution resolution;
    private TaskStatus status;
    private List<TagDTO> tags = new ArrayList<>();
    private String title;

    public TaskDTO() {}

    public PersonDTO getAssignee() {
        return assignee;
    }

    public PersonDTO getCloser() {
        return closer;
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

    public PersonDTO getModifier() {
        return modifier;
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

    public void setAssignee(PersonDTO assignee) {
        this.assignee = assignee;
    }

    public void setCloser(PersonDTO closer) {
        this.closer = closer;
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

    public void setModifier(PersonDTO modifier) {
        this.modifier = modifier;
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
