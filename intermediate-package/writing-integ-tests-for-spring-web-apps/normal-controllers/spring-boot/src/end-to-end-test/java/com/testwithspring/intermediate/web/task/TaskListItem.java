package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.task.TaskStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This class represents a task that is shown
 * on the task list page.
 */
final class TaskListItem {

    private final WebDriver browser;
    private final WebElement itemRootElement;

    TaskListItem(WebDriver browser, WebElement itemRootElement) {
        this.browser = browser;
        this.itemRootElement = itemRootElement;
    }

    /**
     * Returns the id of the shown task.
     * @return
     */
    Long getId() {
        String elementId = itemRootElement.getAttribute("id");
        String taskId = elementId.replace("task-list-item-", "");
        return Long.parseLong(taskId);
    }

    /**
     * Returns the title of shown task.
     * @return
     */
    String getTitle() {
        return itemRootElement.findElement(By.className("task-list-item-title"))
                .getText();
    }

    /**
     * Returns the status of the shown task.
     * @return
     */
    TaskStatus getStatus() {
        WebElement statusElement = itemRootElement.findElement(By.className("task-list-item-status"));

        String status = statusElement.getAttribute("class")
                .replace("task-list-item-status", "")
                .trim()
                .replace("task-status-", "");

        return TaskStatus.valueOf(status);
    }

    /**
     * Opens the view task page that shows the full information of
     * this task.
     * @Return a page object that represents the view task page.
     */
    TaskPage viewTask() {
        WebElement viewTaskLink = itemRootElement.findElement(By.className("task-list-item-title"));

        //We need to create the page object before we click the link because
        //otherwise we cannot get the id of the shown task because our test
        //has navigated to another page.
        TaskPage targetPage = new TaskPage(browser, getId());

        viewTaskLink.click();

        //Because the click() method doesn't wait for the next page to load,
        //we have to make sure that the page is loaded before we can invoke
        //our assertion.
        WebDriverWait wait = new WebDriverWait(browser, 3);
        wait.until(ExpectedConditions.urlToBe(targetPage.getPageUrl()));

        return targetPage;
    }
}
