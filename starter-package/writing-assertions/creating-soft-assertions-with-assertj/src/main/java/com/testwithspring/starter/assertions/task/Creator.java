package com.testwithspring.starter.assertions.task;

/**
 * Identifies the user who created a specific task.
 *
 * @author Petri Kainulainen
 */
public class Creator {

    private final Long userId;

    public Creator(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
