package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.*;
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
public class RenderUpdateTaskPageTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private UpdateTaskPage updateTaskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        updateTaskPage = new UpdateTaskPage(browser, EndToEndTestTasks.WriteLesson.ID);
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).openAsAnonymousUser();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenUpdateTaskPage() {
        UpdateTaskPage shown = updateTaskPage.open();
        assertThat(shown.isOpen()).isTrue();
    }

    @Test
    public void shouldShowDescriptionOfUpdatedTaskOnUpdateTaskForm() {
        UpdateTaskPage shown = updateTaskPage.open();
        TaskForm updateTaskForm = shown.getForm();

        assertThat(updateTaskForm.getTaskDescription()).isEqualTo(EndToEndTestTasks.WriteLesson.DESCRIPTION);
    }

    @Test
    public void shouldShowTitleOfUpdatedTaskOnUpdateTaskForm() {
        UpdateTaskPage shown = updateTaskPage.open();
        TaskForm updateTaskForm = shown.getForm();

        assertThat(updateTaskForm.getTaskTitle()).isEqualTo(EndToEndTestTasks.WriteLesson.TITLE);
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
