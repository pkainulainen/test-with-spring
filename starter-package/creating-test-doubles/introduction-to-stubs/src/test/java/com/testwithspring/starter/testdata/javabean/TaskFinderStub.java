package com.testwithspring.starter.testdata.javabean;

/**
 * This class is a simple stub that returns the configured {@code Task}
 * object when the {@code findById()} method is invoked.
 */
public final class TaskFinderStub implements TaskFinder {

    private final Task found;

    /**
     * Creates a new {@code TaskFinderStub} and configures
     * the returned {@code Task} object.
     * @param found The {@code Task} object that is returned when the {@code findById()} method
     *              is invoked.
     */
    public TaskFinderStub(Task found) {
        this.found = found;
    }

    @Override
    public Task findById(Long id) {
        return found;
    }
}
