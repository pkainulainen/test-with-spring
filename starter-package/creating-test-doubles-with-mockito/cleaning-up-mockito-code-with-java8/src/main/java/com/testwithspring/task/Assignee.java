package com.testwithspring.task;

/**
 * Identifies the assignee of a task.
 *
 * @author Petri Kainulainen
 */
public class Assignee {

    private final Long userId;

    public Assignee(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
