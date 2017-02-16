package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.user.PersonDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * You should consider using a builder because the information
 * of a {@code TaskDTO} object depends from the status of the
 * task. In other words, {@code TaskDTO} objects are not just dummy
 * data containers.
 */
public class TaskDTOBuilder {

    private PersonDTO assignee;
    private PersonDTO closer;
    private PersonDTO creator;
    private String description;
    private Long id;
    private PersonDTO modifier;
    private TaskResolution resolution;
    private TaskStatus status;
    private List<TagDTO> tags = new ArrayList<>();
    private String title;

    public TaskDTOBuilder() {
    }

    public TaskDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TaskDTOBuilder withAssignee(PersonDTO assignee) {
        this.assignee = assignee;
        return this;
    }

    public TaskDTOBuilder withCreator(PersonDTO creator) {
        this.creator = creator;
        return this;
    }

    public TaskDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskDTOBuilder withModifier(PersonDTO modifier) {
        this.modifier = modifier;
        return this;
    }

    public TaskDTOBuilder withResolutionDuplicate(PersonDTO closer) {
        this.closer = closer;
        this.resolution = TaskResolution.DUPLICATE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskDTOBuilder withResolutionDone(PersonDTO closer) {
        this.closer = closer;
        this.resolution = TaskResolution.DONE;
        this.status = TaskStatus.CLOSED;
        return this;
    }

    public TaskDTOBuilder withResolutionWontDo(PersonDTO closer) {
        this.closer = closer;
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

    public TaskDTOBuilder withTags(TagDTO... tags) {
        this.tags = Arrays.asList(tags);
        return this;
    }

    public TaskDTOBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskDTO build() {
        TaskDTO dto = new TaskDTO();
        dto.setAssignee(this.assignee);
        dto.setCloser(this.closer);
        dto.setCreator(this.creator);
        dto.setDescription(this.description);
        dto.setId(this.id);
        dto.setModifier(this.modifier);
        dto.setResolution(this.resolution);
        dto.setStatus(this.status);
        dto.setTags(this.tags);
        dto.setTitle(this.title);
        return dto;
    }
}
