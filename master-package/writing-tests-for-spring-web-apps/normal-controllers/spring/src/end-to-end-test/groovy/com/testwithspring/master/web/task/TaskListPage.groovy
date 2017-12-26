package com.testwithspring.master.web.task

import com.testwithspring.master.WebDriverUrlBuilder
import org.openqa.selenium.By
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
     * Finds the tasks that are shown on the task list page.
     * @return
     */
    List<TaskListItem> getListItems() {
        List<TaskListItem> listItems = []
        browser.findElements(By.className('task-list-item')).each { e ->
            listItems.add(new TaskListItem(browser, e))
        }
        return listItems
    }

    /**
     * Returns the url of the task list page.
     * @return
     */
    String getPageUrl() {
        return pageUrl
    }

    /**
     * @return true if the task list page is open and false otherwise.
     */
    boolean isOpen() {
        return browser.getCurrentUrl() == pageUrl
    }

    /**
     * Opens the task list page.
     * @return The page object that symbolizes the opened page.
     */
    TaskListPage open() {
        browser.get(pageUrl)
        return new TaskListPage(browser)
    }
}
