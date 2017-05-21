package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.SeleniumWait;
import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This page object represents the create task page.
 */
final class CreateTaskPage {

    private final WebDriver browser;
    private final String pageUrl;

    CreateTaskPage(WebDriver browser) {
        this.browser = browser;
        this.pageUrl = WebDriverUrlBuilder.buildFromPath("/task/create");
    }

    /**
     * Returns the page object that represents the create task form.
     * @return
     */
    TaskForm getForm() {
        return new TaskForm(browser);
    }

    /**
     * Returns true if the create task page is open and false otherwise.
     * @return
     */
    boolean isOpen() {
        return browser.getCurrentUrl().equals(pageUrl);
    }

    /**
     * Opens the create task page.
     * @return  The page object that represents the create task page.
     */
    CreateTaskPage open() {
        browser.get(pageUrl);
        waitUntilPageIsOpen();
        return new CreateTaskPage(browser);
    }

    private void waitUntilPageIsOpen() {
        SeleniumWait.waitUntilElementIsClickable(browser, By.id("task-title"));
    }
}
