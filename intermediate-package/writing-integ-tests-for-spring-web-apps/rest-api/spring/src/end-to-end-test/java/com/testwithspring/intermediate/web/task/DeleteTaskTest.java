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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class doesn't have any tests for the timestamps that
 * are shown on the view task page because our test data inserts
 * the current timestamp into our database.
 */
@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class DeleteTaskTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private TaskPage taskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        taskPage = new TaskPage(browser, EndToEndTestTasks.DeleteMe.ID).open();
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).openAsAnonymousUser();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    /**
     * Because we initialize our database into a known state when our
     * web application is started AND the tasks.sql file has one only
     * task that can be deleted, we have to add all essential assertions
     * to one test method.
     *
     * Our other option would be to add more deleted tasks to the tasks.sql
     * file, but I decided to not do it because I can add all essential
     * assertions to this test method and keep the setup code in setup method.
     * If I would use more than one deleted task, I would have to open view task
     * page in my test methods. This makes the test methods a bit too messy
     * for my taste.
     */
    @Test
    public void shouldShowTaskListPageThatDoesNotDisplayDeletedTask() {
        TaskListPage shown = taskPage.getTaskActions().deleteTask();

        assertThat(shown.isOpen()).isTrue();

        List<Long> shownTaskIds = shown.getListItems().stream()
                .map(TaskListItem::getId)
                .collect(Collectors.toList());
        assertThat(shownTaskIds).doesNotContain(EndToEndTestTasks.DeleteMe.ID);
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
