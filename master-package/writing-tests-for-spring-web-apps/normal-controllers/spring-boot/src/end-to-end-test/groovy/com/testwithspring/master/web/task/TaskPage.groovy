package com.testwithspring.master.web.task

import com.testwithspring.master.WebDriverUrlBuilder
import org.openqa.selenium.WebDriver

/**
 * This page objects represent the view task page.
 */
final class TaskPage {

    private final WebDriver browser
    private final String pageUrl
    private final Long taskId

    TaskPage(WebDriver browser, Long taskId) {
        this.browser = browser
        this.pageUrl = WebDriverUrlBuilder.buildFromPath('/task/%d', taskId)
        this.taskId = taskId
    }


    /**
     * Returns the URL of the view task page.
     * @return
     */
    String getPageUrl() {
        return pageUrl
    }

    /**
     * @return true if the task page is open and false otherwise.
     */
    boolean isOpen() {
        return browser.getCurrentUrl() == pageUrl
    }
}
