package com.testwithspring.starter.testdata.javabean;

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
}
