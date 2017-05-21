package com.testwithspring.intermediate.web.task;


import com.testwithspring.intermediate.SeleniumWait;
import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This page objects represents the view task page.
 */
final class TaskPage {

    private final WebDriver browser;
    private final String pageUrl;
    private final Long taskId;

    TaskPage(WebDriver browser, Long taskId) {
        this.browser = browser;
        this.pageUrl = WebDriverUrlBuilder.buildFromPath("/task/%d", taskId);
        this.taskId = taskId;
    }

    /**
     * Returns a page object that allows you to click the links found from the
     * task list page.
     * @return
     */
    TaskActions getTaskActions() {
        return new TaskActions(browser,
                browser.findElement(By.id("task-action-links")),
                taskId
        );
    }

    /**
     * Returns a page object that allows you to find the field values
     * of the task's life cycle fields.
     * @return
     */
    TaskLifeCycleFields getTaskLifeCycleFields() {
        return new TaskLifeCycleFields(browser.findElement(By.id("lifecycle-fields")));
    }

    /**
     * Returns the description of the shown task.
     * @return
     */
    String getTaskDescription() {
        return browser.findElement(By.id("task-description")).getText();
    }

    /**
     * returns the title of the shown task.
     * @return
     */
    String getTaskTitle() {
        return browser.findElement(By.id("task-title")).getText();
    }

    /**
     * Opens the view task page.
     * @return The page object that symbolizes the opened page.
     * @throws RuntimeException if the task is null (this page was opened after a task was created).
     */
    TaskPage open() {
        if (taskId == null) {
            throw new RuntimeException("Cannot open view task page because the task id is not specified");
        }

        browser.get(pageUrl);
        waitUntilPageIsOpen();
        return new TaskPage(browser, taskId);
    }

    /**
     * @return true if the task page is open and false otherwise.
     */
    boolean isOpen() {
        return browser.getCurrentUrl().equals(pageUrl);
    }

    void waitUntilPageIsOpen() {
        SeleniumWait.waitUntilElementIsClickable(browser, By.id("update-task-link"));
    }

    /**
     * @return true if the task page is open and false otherwise. Note that this
     * method doesn't compare the full URL of the task page, and this method should
     * be used only to verify that the view task page was opened after a new task
     * was created.
     */
    boolean isOpenWithUnknownTaskId() {
        String pageUrlWithoutId = WebDriverUrlBuilder.buildFromPath("/task/");
        String taskId = browser.getCurrentUrl().replace(pageUrlWithoutId, "");
        return StringUtils.isNumeric(taskId);
    }
}
