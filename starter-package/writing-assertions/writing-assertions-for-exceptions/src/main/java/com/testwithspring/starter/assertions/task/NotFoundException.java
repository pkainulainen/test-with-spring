package com.testwithspring.starter.assertions.task;

public class NotFoundException extends RuntimeException {

    private Long id;

    NotFoundException(Long id) {
        super(String.format("No entity found with id: %d", id));
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
