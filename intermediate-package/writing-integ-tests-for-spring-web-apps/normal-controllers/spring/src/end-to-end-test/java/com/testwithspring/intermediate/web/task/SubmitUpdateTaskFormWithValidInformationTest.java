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

import static com.testwithspring.intermediate.EndToEndTestUsers.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class SubmitUpdateTaskFormWithValidInformationTest {

    private static final String NEW_DESCRIPTION = "The old lesson was not good";
    private static final String NEW_TITLE = "Rewrite an existing lesson";

    @SeleniumWebDriver
    private WebDriver browser;

    private UpdateTaskPage updateTaskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        updateTaskPage = new UpdateTaskPage(browser, EndToEndTestTasks.WriteLesson.ID).open();
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).open();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenViewTaskPage() {
        TaskPage viewTaskPage = updateTask();
        assertThat(viewTaskPage.isOpen()).isTrue();
    }

    @Test
    public void shouldShowUpdatedDescription() {
        TaskPage viewTaskPage = updateTask();
        assertThat(viewTaskPage.getTaskDescription()).isEqualTo(NEW_DESCRIPTION);
    }

    @Test
    public void shouldShowUpdatedTitle() {
        TaskPage viewTaskPage = updateTask();
        assertThat(viewTaskPage.getTaskTitle()).isEqualTo(NEW_TITLE);
    }

    @Test
    public void shouldNotShowAssigneeName() {
        TaskPage viewTaskPage = updateTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.isAssigneeNameVisible()).isFalse();
    }

    @Test
    public void shouldShowOriginalCreatorNameOfUpdatedTask() {
        TaskPage viewTaskPage = updateTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getCreatorName()).isEqualTo(EndToEndTestTasks.WriteLesson.Creator.NAME);
    }

    @Test
    public void shouldShowNewModifierNameOfUpdatedTask() {
        TaskPage viewTaskPage = updateTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getModifierName()).isEqualTo(JohnDoe.NAME);
    }

    @Test
    public void shouldNotShowClosedTaskFields() {
        TaskPage viewTaskPage = updateTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.areClosedTaskFieldsVisible()).isFalse();
    }

    private TaskPage updateTask() {
        TaskForm updateTaskForm = updateTaskPage.getForm();
        updateTaskForm.typeTitle(NEW_TITLE);
        updateTaskForm.typeDescription(NEW_DESCRIPTION);
        return updateTaskForm.submitTaskForm();
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
