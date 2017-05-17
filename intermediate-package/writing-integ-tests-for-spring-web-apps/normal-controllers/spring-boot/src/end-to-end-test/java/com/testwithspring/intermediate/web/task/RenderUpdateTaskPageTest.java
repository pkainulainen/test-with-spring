package com.testwithspring.intermediate.web.task;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.EndToEndTestTasks;
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
public class RenderUpdateTaskPageTest {

    @Autowired
    private WebDriver browser;

    private UpdateTaskPage updateTaskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        updateTaskPage = new UpdateTaskPage(browser, EndToEndTestTasks.WriteLesson.ID);
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).open();
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
