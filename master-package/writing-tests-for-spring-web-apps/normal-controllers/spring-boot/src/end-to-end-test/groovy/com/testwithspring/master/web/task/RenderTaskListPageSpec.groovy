package com.testwithspring.master.web.task

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.EndToEndTestTasks
import com.testwithspring.master.NavigationBar
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.TaskTrackerApplication
import com.testwithspring.master.config.Profiles
import com.testwithspring.master.web.login.LoginPage
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import spock.lang.Specification

import static com.testwithspring.master.EndToEndTestUsers.*
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD

@SpringBootTest(classes = TaskTrackerApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@SeleniumTest(driver = ChromeDriver.class)
@SqlGroup([
        @Sql(value = [
                'classpath:/com/testwithspring/master/users.sql',
                'classpath:/com/testwithspring/master/tasks.sql'
        ]),
        @Sql(
                value = 'classpath:/com/testwithspring/master/cleandb.sql',
                executionPhase = AFTER_TEST_METHOD
        )
])
@ActiveProfiles(Profiles.END_TO_END_TEST)
@Category(EndToEndTest.class)
class RenderTaskListPageSpec extends Specification {

    @Autowired
    WebDriver browser

    TaskListPage taskListPage

    def setup() {
        taskListPage = new TaskListPage(browser)
    }

    def 'Open task list page as a registered user'() {

        TaskListPage shownPage

        given: 'A user is authenticated successfully'
        LoginPage loginPage = new LoginPage(browser).open()
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'A user opens the task list page'
        shownPage = taskListPage.open()

        then: 'Should open the task list page'
        shownPage.isOpen()

        and: 'Should display task list that has two tasks'
        def tasks = shownPage.getListItems()
        tasks.size() == 2

        and: 'Should show the correct information of the first task'
        def firstTask = tasks.get(0)

        firstTask.id == EndToEndTestTasks.WriteExampleApp.ID
        firstTask.title == EndToEndTestTasks.WriteExampleApp.TITLE
        firstTask.status == EndToEndTestTasks.WriteExampleApp.STATUS

        and: 'Should show the correct information of the second task'
        def secondTask = tasks.get(1)

        secondTask.id == EndToEndTestTasks.WriteLesson.ID
        secondTask.title == EndToEndTestTasks.WriteLesson.TITLE
        secondTask.status == EndToEndTestTasks.WriteLesson.STATUS

        and: 'A user must be able to navigate to the view task page'
        def taskPage = firstTask.viewTask()
        taskPage.isOpen()
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
