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
        loginUser();
        taskListPage = new TaskListPage(browser);
    }

    private void loginUser() {
        LoginPage loginPage = new LoginPage(browser).open();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenTaskListPage() {
        TaskListPage shown = taskListPage.open();
        assertThat(shown.isOpen());
    }

    @After
    public void logoutUser() {
        new NavigationBar(browser).logout();
    }
}
