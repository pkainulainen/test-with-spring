package com.testwithspring.intermediate.web.task;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This page object provides methods that are used
 * to click the links found from the view task page.
 */
final class TaskActions {

    private final WebDriver browser;
    private final WebElement rootElement;
    private final Long taskId;

    TaskActions(WebDriver browser, WebElement rootElement, Long taskId) {
        this.browser = browser;
        this.rootElement = rootElement;
        this.taskId = taskId;
    }

    /**
     * Clicks the delete task link. Note that this method assumes
     * that the task can be deleted successfully.
     * @return  The page object that represents the task list page.
     */
    TaskListPage deleteTask() {
        DeleteTaskConfirmationDialog confirmationDialog = new DeleteTaskConfirmationDialog(browser, rootElement).open();
        return confirmationDialog.deleteTask();
    }

    /**
     * Clicks update task link. Note that this method assumes that task is
     * found from the database.
     * @return  The page object that represents the update task page.
     */
    UpdateTaskPage updateTask() {
        WebElement updateTask = rootElement.findElement(By.id("update-task-link"));
        updateTask.click();

        UpdateTaskPage targetPage = new UpdateTaskPage(browser, taskId);
        targetPage.waitUntilPageIsOpen();

        return targetPage;
    }
}
