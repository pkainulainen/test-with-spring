package com.testwithspring.starter.assertions.task;

public class TaskFinder {

    public Task findById(Long id) {
        throw new NotFoundException(id);
    }
}
