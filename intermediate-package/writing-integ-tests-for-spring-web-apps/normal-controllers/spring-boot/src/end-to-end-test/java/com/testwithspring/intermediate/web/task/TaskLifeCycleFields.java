package com.testwithspring.intermediate.web.task;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * This page object provides methods that are used to find
 * the values of the task's life cycle fields such creator name
 * and creation time.
 */
final class TaskLifeCycleFields {

    private final WebElement rootElement;

    TaskLifeCycleFields(WebElement rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * Returns the name of the user to whom the task is assigned.
     * @return
     */
    String getAssigneeName() {
        return rootElement.findElement(By.id("assignee-name")).getText();
    }

    /**
     * Returns the name of the user who closed the shown task.
     * @return
     */
    String getCloserName() {
        return rootElement.findElement(By.id("closer-name")).getText();
    }

    /**
     * Returns the name of the user who created the shown task.
     * @return
     */
    String getCreatorName() {
        return rootElement.findElement(By.id("creator-name")).getText();
    }

    /**
     * Returns the name of the user who made the last changes to
     * the information of the shown task.
     * @return
     */
    String getModifierName() {
        return rootElement.findElement(By.id("modifier-name")).getText();
    }

    /**
     * Returns true if the assignee name is visible and false otherwise.
     * @return
     */
    boolean isAssigneeNameVisible() {
        return rootElement.findElements(By.id("assignee-name")).size() == 1;
    }

    /**
     * Returns true if the closer name and closing time are shown and false otherwise.
     * @return
     */
    boolean areClosedTaskFieldsVisible() {
        return rootElement.findElements(By.id("closed-task-fields")).size() == 1;
    }
}
