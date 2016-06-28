package com.testwithspring.starter.testdata.javabean;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskRepositoryFakeTest {

    private static final String TITLE = "task";

    private TaskRepository repository;

    @Before
    public void createRepository() {
        repository = new TaskRepositoryFake();
    }

    /**
     * This test case demonstrates how our fake object works. I don't
     * recommend writing test cases which have multiple assertions
     * and initialize the test data inside the test method.
     */
    @Test
    public void saveAndFindTask() {
        Task newTask = new TaskBuilder()
                .withTitle(TITLE)
                .build();

        Task saved = repository.save(newTask);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(TITLE);

        Task found = repository.findById(saved.getId());
        assertThat(found.getId()).isNotNull();
        assertThat(found.getTitle()).isEqualTo(TITLE);
    }
}
