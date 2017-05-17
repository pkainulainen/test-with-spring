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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
public class DeleteTaskTest {

    @Autowired
    private WebDriver browser;

    private TaskPage taskPage;

    @Before
    public void configureTestCases() {
        logUserIn();
        taskPage = new TaskPage(browser, EndToEndTestTasks.WriteLesson.ID).open();
    }

    private void logUserIn() {
        LoginPage loginPage = new LoginPage(browser).open();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldRenderTaskListPage() {
        TaskListPage shown = taskPage.getTaskActions().deleteTask();
        assertThat(shown.isOpen()).isTrue();
    }

    @Test
    public void shouldShowTaskListThatHasOneTask() {
        TaskListPage shown = taskPage.getTaskActions().deleteTask();
        assertThat(shown.getListItems()).hasSize(1);
    }

    @Test
    public void shouldShowTaskListDoesNotDisplayDeletedTask() {
        TaskListPage shown = taskPage.getTaskActions().deleteTask();

        assertThat(shown.isOpen()).isTrue();

        List<Long> shownTaskIds = shown.getListItems().stream()
                .map(TaskListItem::getId)
                .collect(Collectors.toList());
        assertThat(shownTaskIds).containsExactly(EndToEndTestTasks.WriteExampleApp.ID);
    }

    @After
    public void logUserOut() {
        new NavigationBar(browser).logUserOut();
    }
}
