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

import java.util.List;

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
public class RenderTaskListPageTest {

    @Autowired
    private WebDriver browser;

    private TaskListPage taskListPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        taskListPage = new TaskListPage(browser);
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).open();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenTaskListPage() {
        TaskListPage shown = taskListPage.open();
        assertThat(shown.isOpen()).isTrue();
    }

    @Test
    public void shouldShowTaskListThatHasTwoTasks() {
        TaskListPage shown = taskListPage.open();
        List<TaskListItem> tasks = shown.getListItems();
        assertThat(tasks.size()).isEqualTo(2);
    }

    @Test
    public void shouldShowCorrectInformationOfFirstTask() {
        TaskListPage shown = taskListPage.open();
        TaskListItem first = shown.getListItems().get(0);

        assertThat(first.getId()).isEqualByComparingTo(EndToEndTestTasks.WriteExampleApp.ID);
        assertThat(first.getTitle()).isEqualTo(EndToEndTestTasks.WriteExampleApp.TITLE);
        assertThat(first.getStatus()).isEqualTo(EndToEndTestTasks.WriteExampleApp.STATUS);
    }

    @Test
    public void shouldShowCorrectInformationOfSecondTask() {
        TaskListPage shown = taskListPage.open();
        TaskListItem first = shown.getListItems().get(1);

        assertThat(first.getId()).isEqualByComparingTo(EndToEndTestTasks.WriteLesson.ID);
        assertThat(first.getTitle()).isEqualTo(EndToEndTestTasks.WriteLesson.TITLE);
        assertThat(first.getStatus()).isEqualTo(EndToEndTestTasks.WriteLesson.STATUS);
    }

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
