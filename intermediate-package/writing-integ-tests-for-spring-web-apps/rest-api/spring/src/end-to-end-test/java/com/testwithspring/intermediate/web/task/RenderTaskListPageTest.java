package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.EndToEndTestUsers.JohnDoe;
import com.testwithspring.intermediate.web.NavigationBar;
import com.testwithspring.intermediate.web.login.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class doesn't have any tests for the timestamps that
 * are shown on the view task page because our test data inserts
 * the current timestamp into our database.
 */
@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderTaskListPageTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private TaskListPage taskListPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        taskListPage = new TaskListPage(browser);
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).openAsAnonymousUser();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenTaskListPage() {
        TaskListPage shown = taskListPage.open();
        assertThat(shown.isOpen()).isTrue();
    }

    /**
     * Because we initialize our database into a known state when
     * our web application is started, and we cannot the order in
     * which our end-to-end are run, we cannot know the exact number
     * of tasks that are shown on the task list page.
     *
     * Because our SQL script inserts three rows into the tasks table,
     * our database as always at least two tasks. This means that the
     * task list page always shows at least two tasks.
     */
    @Test
    public void shouldShowAtLeastTwoTasks() {
        TaskListPage shown = taskListPage.open();
        List<TaskListItem> tasks = shown.getListItems();
        assertThat(tasks.size()).isGreaterThanOrEqualTo(2);
    }

    /**
     * Because we initialize our database into a known state when
     * our web application is started, and we cannot the order in
     * which our end-to-end are run, we can write assertions only
     * for the task that is not updated by our other end-to-end tests.
     *
     * The first task that is shown on the task list page is not updated
     * by our other end-to-end tests. If we want verify that the task list
     * page shows the correct information of a task, we can verify that
     * first task of our task list is rendered correctly.
     */
    @Test
    public void shouldShowCorrectInformationOfFirstTask() {
        TaskListPage shown = taskListPage.open();
        TaskListItem first = shown.getListItems().get(0);

        assertThat(first.getId()).isEqualByComparingTo(EndToEndTestTasks.WriteExampleApp.ID);
        assertThat(first.getTitle()).isEqualTo(EndToEndTestTasks.WriteExampleApp.TITLE);
        assertThat(first.getStatus()).isEqualTo(EndToEndTestTasks.WriteExampleApp.STATUS);
    }

    /**
     * It's a good idea to verify that the links found from the tested
     * HTML page are working as expected. This test ensures that we
     * can navigate fromt he task list page to the view task page.
     */
    @Test
    public void shouldAllowUsToNavigateToViewTaskPage() {
        TaskListPage taskListPage = this.taskListPage.open();
        TaskListItem first = taskListPage.getListItems().get(0);

        TaskPage shown = first.viewTask();
        assertThat(shown.isOpen()).isTrue();
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
