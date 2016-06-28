package com.testwithspring.starter.testdata.javabean;

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
}
