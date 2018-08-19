package com.testwithspring.starter.testdata.javabean;

/**
 * Contains the information of a single task.
 *
 * @author Petri Kainulainen
 */
public class Task {

    private Long id;
    private Assignee assignee;
    private Closer closer;
    private Creator creator;
    private String description;
    private Modifier modifier;
    private String title;
    private TaskStatus status;
    private TaskResolution resolution;

    public Task() {

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

    public Modifier getModifier() {
        return modifier;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public void setCloser(Closer closer) {
        this.closer = closer;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setResolution(TaskResolution resolution) {
        this.resolution = resolution;
    }
}
