package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.*;
import com.testwithspring.intermediate.EndToEndTestUsers.JohnDoe;
import com.testwithspring.intermediate.config.Profiles;
import com.testwithspring.intermediate.web.NavigationBar;
import com.testwithspring.intermediate.web.login.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TaskTrackerApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@SeleniumTest(driver = ChromeDriver.class)
@Sql(value = {
        "classpath:/com/testwithspring/intermediate/users.sql",
        "classpath:/com/testwithspring/intermediate/tasks.sql"
})
@Sql(
        value = "classpath:/com/testwithspring/intermediate/cleandb.sql",
        executionPhase = AFTER_TEST_METHOD
)
@ActiveProfiles(Profiles.END_TO_END_TEST)
@Category(EndToEndTest.class)
public class RenderViewTaskPageWhenTaskIsClosedTest {

    @Autowired
    private WebDriver browser;

    private TaskPage taskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        taskPage = new TaskPage(browser, EndToEndTestTasks.WriteExampleApp.ID);
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).open();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenViewTaskPage() {
        TaskPage shown = taskPage.open();
        assertThat(shown.isOpen()).isTrue();
    }

    @Test
    public void shouldShowTitleOfViewedTask() {
        TaskPage shown = taskPage.open();
        assertThat(shown.getTaskTitle()).isEqualTo(EndToEndTestTasks.WriteExampleApp.TITLE);
    }

    @Test
    public void shouldShowDescriptionOfViewedTask() {
        TaskPage shown = taskPage.open();
        assertThat(shown.getTaskDescription()).isEqualTo(EndToEndTestTasks.WriteExampleApp.DESCRIPTION);
    }

    @Test
    public void shouldShowAssigneeInformationOfViewedTask() {
        TaskPage shown = taskPage.open();
        TaskLifeCycleFields lifeCycleFields = shown.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.isAssigneeNameVisible()).isTrue();
    }

    @Test
    public void shouldShowAssigneeNameOfViewedTask() {
        TaskPage shown = taskPage.open();
        TaskLifeCycleFields lifeCycleFields = shown.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getAssigneeName()).isEqualTo(EndToEndTestTasks.WriteExampleApp.Assignee.NAME);
    }

    @Test
    public void shouldShowCreatorNameOfViewedTask() {
        TaskPage shown = taskPage.open();
        TaskLifeCycleFields lifeCycleFields = shown.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getCreatorName()).isEqualTo(EndToEndTestTasks.WriteExampleApp.Creator.NAME);
    }

    @Test
    public void shouldShowModifierNameOfViewedTask() {
        TaskPage shown = taskPage.open();
        TaskLifeCycleFields lifeCycleFields = shown.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getModifierName()).isEqualTo(EndToEndTestTasks.WriteExampleApp.Modifier.NAME);
    }

    @Test
    public void shouldShowClosedTaskLifeCycleFields() {
        TaskPage shown = taskPage.open();
        TaskLifeCycleFields lifeCycleFields = shown.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.areClosedTaskFieldsVisible()).isTrue();
    }

    @Test
    public void shouldShowCloserNameOfViewedTask() {
        TaskPage shown = taskPage.open();
        TaskLifeCycleFields lifeCycleFields = shown.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getCloserName()).isEqualTo(EndToEndTestTasks.WriteExampleApp.Closer.NAME);
    }

    @Test
    public void shouldAllowUsToNavigateToUpdateTaskPage() {
        TaskPage shown = taskPage.open();
        TaskActions actions = shown.getTaskActions();

        UpdateTaskPage targetPage = actions.updateTask();
        assertThat(targetPage.isOpen()).isTrue();
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
