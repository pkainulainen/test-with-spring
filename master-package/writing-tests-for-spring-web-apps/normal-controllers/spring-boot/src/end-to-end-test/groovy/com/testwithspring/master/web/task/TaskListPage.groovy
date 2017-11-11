package com.testwithspring.master.web.task

import com.testwithspring.master.WebDriverUrlBuilder
import org.openqa.selenium.WebDriver

/**
 * This page object is used to interact with the task list page.
 */
final class TaskListPage {

    private final WebDriver browser
    private final String pageUrl

    TaskListPage(WebDriver browser) {
        this.browser = browser
        this.pageUrl = WebDriverUrlBuilder.buildFromPath('/')
    }

    /**
     * @return true if the task list page is open and false otherwise.
     */
    boolean isOpen() {
        return browser.getCurrentUrl() == pageUrl
    }
}
