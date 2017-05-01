package com.testwithspring.intermediate.web.login;

import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.web.NavigationBar;
import com.testwithspring.intermediate.web.task.TaskListPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.testwithspring.intermediate.EndToEndTestUsers.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class SubmitLoginFormWithCorrectUsernameAndPasswordTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private LoginPage loginPage;

    @Before
    public void openLoginPage() {
        loginPage = new LoginPage(browser).open();
    }

    @Test
    public void shouldRenderTaskListPage() {
        TaskListPage shownPage = loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
        assertThat(shownPage.isOpen()).isTrue();
    }

    @After
    public void logoutUser() {
        new NavigationBar(browser).logUserOut();
    }
}
