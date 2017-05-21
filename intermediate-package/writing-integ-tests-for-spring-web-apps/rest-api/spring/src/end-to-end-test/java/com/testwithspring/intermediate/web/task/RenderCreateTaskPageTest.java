package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import com.testwithspring.intermediate.SeleniumTestRunner;
import com.testwithspring.intermediate.SeleniumWebDriver;
import com.testwithspring.intermediate.web.NavigationBar;
import com.testwithspring.intermediate.web.login.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.testwithspring.intermediate.EndToEndTestUsers.JohnDoe;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderCreateTaskPageTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private CreateTaskPage createTaskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        createTaskPage = new CreateTaskPage(browser);
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).openAsAnonymousUser();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenCreateTaskPage() {
        CreateTaskPage shown = createTaskPage.open();
        assertThat(shown.isOpen()).isTrue();
    }

    @Test
    public void shouldEmptyDescriptionOnCreateTaskForm() {
        CreateTaskPage shown = createTaskPage.open();
        TaskForm createTaskForm = shown.getForm();

        assertThat(createTaskForm.getTaskDescription()).isEmpty();
    }

    @Test
    public void shouldShowEmptyTitleOnCreateTaskForm() {
        CreateTaskPage shown = createTaskPage.open();
        TaskForm createTaskForm = shown.getForm();

        assertThat(createTaskForm.getTaskTitle()).isEmpty();
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
