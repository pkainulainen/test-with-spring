package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.WebDriver;

/**
 * This page object is use dto interact with the task list page.
 */
public final class TaskListPage {

    private final WebDriver browser;
    private final String pageUrl;

    public TaskListPage(WebDriver browser) {
        this.browser = browser;
        this.pageUrl = WebDriverUrlBuilder.buildFromRelativeUrl("/");
    }

    /**
     * @return true if the task list page is open and false otherwise.
     */
    public boolean isOpen() {
        return browser.getCurrentUrl().equals(pageUrl);
    }
}
