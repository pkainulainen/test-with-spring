package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * This page object is use dto interact with the task list page.
 */
public final class TaskListPage {

    private final WebDriver browser;
    private final String pageUrl;

    public TaskListPage(WebDriver browser) {
        this.browser = browser;
        this.pageUrl = WebDriverUrlBuilder.buildFromPath("/");
    }

    /**
     * Finds the tasks that are shown on the task list page.
     * @return
     */
    List<TaskListItem> getListItems() {
        List<TaskListItem> listItems = new ArrayList<>();
        browser.findElements(By.className("task-list-item")).forEach(e -> listItems.add(new TaskListItem(browser, e)));
        return listItems;
    }

    /**
     * Returns the url of the task list page.
     * @return
     */
    String getPageUrl() {
        return pageUrl;
    }

    /**
     * @return true if the task list page is open and false otherwise.
     */
    public boolean isOpen() {
        return browser.getCurrentUrl().equals(pageUrl);
    }

    /**
     * Opens the task list page.
     * @return The page object that symbolizes the opened page.
     */
    public TaskListPage open() {
        browser.get(pageUrl);
        return new TaskListPage(browser);
    }
}
