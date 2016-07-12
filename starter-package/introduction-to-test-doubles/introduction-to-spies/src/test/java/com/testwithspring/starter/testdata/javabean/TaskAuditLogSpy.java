package com.testwithspring.starter.testdata.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * This a simple spy that records the invocation count and
 * the method parameters of the {@code logTask()} method.
 *
 * This spy also provides getter methods that are used to obtain
 * the recorded information.
 */
public final class TaskAuditLogSpy implements TaskAuditLog {

    private int invocationCount;
    private List<Task> auditedTasks;

    public TaskAuditLogSpy() {
        invocationCount = 0;
        auditedTasks = new ArrayList<>();
    }

    @Override
    public void logTask(Task task) {
        invocationCount++;
        auditedTasks.add(task);
    }

    public int getInvocationCount() {
        return invocationCount;
    }

    public List<Task> getAuditedTasks() {
        return auditedTasks;
    }
}
