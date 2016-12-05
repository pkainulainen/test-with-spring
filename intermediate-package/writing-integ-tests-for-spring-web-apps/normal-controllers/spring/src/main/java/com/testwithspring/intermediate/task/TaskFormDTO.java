package com.testwithspring.intermediate.task;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class TaskFormDTO {

    private Long id;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 100)
    private String title;

    public TaskFormDTO() {

    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("description", this.description)
                .append("title", this.title)
                .build();
    }
}