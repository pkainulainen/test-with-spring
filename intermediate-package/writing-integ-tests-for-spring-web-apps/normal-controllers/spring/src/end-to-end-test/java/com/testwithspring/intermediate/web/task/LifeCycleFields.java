package com.testwithspring.intermediate.web.task;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This page object provides methods that are used to find
 * the values of the task's life cycle fields such creator name
 * and creation time.
 */
final class LifeCycleFields {

    private final WebDriver browser;

    LifeCycleFields(WebDriver browser) {
        this.browser = browser;
    }

    /**
     * Returns the name of the user who closed the shown task.
     * @return
     */
    String getCloserName() {
        return browser.findElement(By.id("closer-name")).getText();
    }

    /**
     * Returns the name of the user who created the shown task.
     * @return
     */
    String getCreatorName() {
        return browser.findElement(By.id("creator-name")).getText();
    }

    /**
     * Returns the name of the user who made the last changes to
     * the information of the shown task.
     * @return
     */
    String getModifierName() {
        return browser.findElement(By.id("modifier-name")).getText();
    }

    /**
     * Returns true if the closer name and closing time are shown and false otherwise.
     * @return
     */
    boolean areClosedTaskFieldsVisible() {
        return browser.findElements(By.id("closed-task-fields")).size() == 1;
    }
}
