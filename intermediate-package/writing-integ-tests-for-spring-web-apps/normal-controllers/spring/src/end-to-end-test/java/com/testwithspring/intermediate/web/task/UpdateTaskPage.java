package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.WebDriver;

/**
 * This page object represents the update task page.
 */
final class UpdateTaskPage {

    private final WebDriver browser;
    private final String pageUrl;
    private final Long taskId;

    UpdateTaskPage(WebDriver browser, Long taskId) {
        this.browser = browser;
        this.pageUrl = WebDriverUrlBuilder.buildFromPath("/task/%d/update", taskId);
        this.taskId = taskId;
    }

    /**
     * Returns the page object that represents the update task form.
     * @return
     */
    TaskForm getForm() {
        return new TaskForm(browser, "update-task-form");
    }

    /**
     * Returns the URL of the update task page.
     * @return
     */
    String getPageUrl() {
        return pageUrl;
    }

    /**
     * Returns true if the update task page is open and false otherwise.
     * @return
     */
    boolean isOpen() {
        return browser.getCurrentUrl().equals(pageUrl);
    }

    /**
     * Opens the update task page.
     * @return  The page object that represents the update task page.
     */
    UpdateTaskPage open() {
        browser.get(pageUrl);
        return new UpdateTaskPage(browser, taskId);
    }
}
