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
        WebElement deleteLink = rootElement.findElement(By.id("delete-task-link"));
        deleteLink.click();

        TaskListPage targetPage = new TaskListPage(browser);

        //Because the click() method doesn't wait for the next page to load,
        //we have to make sure that the page is loaded before we can invoke
        //our assertion.
        WebDriverWait wait = new WebDriverWait(browser, 3);
        wait.until(ExpectedConditions.urlToBe(targetPage.getPageUrl()));

        return targetPage;
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

        //Because the click() method doesn't wait for the next page to load,
        //we have to make sure that the page is loaded before we can invoke
        //our assertion.
        WebDriverWait wait = new WebDriverWait(browser, 3);
        wait.until(ExpectedConditions.urlToBe(targetPage.getPageUrl()));

        return targetPage;
    }
}


