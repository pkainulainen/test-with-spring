package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.EndToEndTestUsers.JohnDoe;
import com.testwithspring.intermediate.SeleniumTest;
import com.testwithspring.intermediate.TaskTrackerApplication;
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
public class SubmitCreateTaskFormWithValidInformationTest {

    private static final String DESCRIPTION = "This is a new lesson";
    private static final String TITLE = "Write an extra lesson";

    @Autowired
    private WebDriver browser;

    private CreateTaskPage createTaskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        createTaskPage = new CreateTaskPage(browser).open();
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).open();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenViewTaskPage() {
        TaskPage viewTaskPage = createTask();
        assertThat(viewTaskPage.isOpen()).isTrue();
    }

    @Test
    public void shouldShowDescriptionOfCreatedTask() {
        TaskPage viewTaskPage = createTask();
        assertThat(viewTaskPage.getTaskDescription()).isEqualTo(DESCRIPTION);
    }

    @Test
    public void shouldShowTitleOfCreatedTask() {
        TaskPage viewTaskPage = createTask();
        assertThat(viewTaskPage.getTaskTitle()).isEqualTo(TITLE);
    }

    @Test
    public void shouldNotShowAssigneeName() {
        TaskPage viewTaskPage = createTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.isAssigneeNameVisible()).isFalse();
    }

    @Test
    public void shouldShowCreatorNameOfCreatedTask() {
        TaskPage viewTaskPage = createTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getCreatorName()).isEqualTo(JohnDoe.NAME);
    }

    @Test
    public void shouldShowModifierNameOfCreatedTask() {
        TaskPage viewTaskPage = createTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.getModifierName()).isEqualTo(JohnDoe.NAME);
    }

    @Test
    public void shouldNotShowClosedTaskFields() {
        TaskPage viewTaskPage = createTask();
        TaskLifeCycleFields lifeCycleFields = viewTaskPage.getTaskLifeCycleFields();

        assertThat(lifeCycleFields.areClosedTaskFieldsVisible()).isFalse();
    }

    private TaskPage createTask() {
        TaskForm createTaskForm = createTaskPage.getForm();
        createTaskForm.typeTitle(TITLE);
        createTaskForm.typeDescription(DESCRIPTION);
        return createTaskForm.submitTaskForm();
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
