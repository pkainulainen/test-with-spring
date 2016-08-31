package com.testwithspring.starter.springboot.task;

/**
 * This DTO Contains the information that is shown on list pages.
 */
public class TaskListDTO {

    private Long id;
    private String title;
    private TaskStatus status;

    public TaskListDTO() {}

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
