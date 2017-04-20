package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.WebDriver;

/**
 * This page object is use dto interact with the task list page.
 */
final class TaskListPage {

    private final WebDriver browser;

    TaskListPage(WebDriver browser) {
        this.browser = browser;
    }

    /**
     * Returns the url address of the task list page.
     * @return
     */
    String getPageUrl() {
        return WebDriverUrlBuilder.buildFromRelativeUrl("/");
    }
}
