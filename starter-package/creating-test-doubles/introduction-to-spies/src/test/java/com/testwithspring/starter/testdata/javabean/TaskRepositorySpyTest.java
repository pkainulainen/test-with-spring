package com.testwithspring.starter.testdata.javabean;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class demonstrates how our spy works.
 */
public class TaskRepositorySpyTest {

    private static final String TITLE = "example title";

    private TaskAuditLogSpy auditLog;

    @Before
    public void createSpy() {
        auditLog = new TaskAuditLogSpy();
    }

    @Test
    public void logTask() {
        Task task = new TaskBuilder()
                .withTitle(TITLE)
                .build();

        auditLog.logTask(task);

        int actualInvocationCount = auditLog.getInvocationCount();
        assertThat(actualInvocationCount).isEqualByComparingTo(1);

        List<Task> auditedTasks = auditLog.getAuditedTasks();
        assertThat(auditedTasks).containsExactly(task);
    }
}
