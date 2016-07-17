package com.testwithspring.task;

/**
 * Identifies the user who closed the task.
 *
 * @author Petri Kainulainen
 */
public class Closer {

    private final Long userId;

    public Closer(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
