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

import static com.testwithspring.master.EndToEndTestUsers.JohnDoe
import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.hasSize
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
class DeleteTaskSpec extends Specification {

    @Autowired
    WebDriver browser

    TaskPage viewTaskPage

    def setup() {
        viewTaskPage = new TaskPage(browser, EndToEndTestTasks.WriteLesson.ID)
    }

    def 'Delete task as a registered user'() {

        TaskListPage shownPage

        given: 'A user is authenticated successfully'
        LoginPage loginPage = new LoginPage(browser).open()
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'A user open the view task page and deletes the task'
        shownPage = viewTaskPage.open().getTaskActions().deleteTask()

        then: 'Should open the task list page'
        shownPage.isOpen()

        and: 'Should display task list that has one task'
        def taskList = shownPage.getListItems()
        taskList hasSize(1)

        and: 'Should not display the deleted task'
        List<Long> shownTaskIds = []
        taskList.each {t ->
            shownTaskIds.add(t.id)
        }
        shownTaskIds contains(EndToEndTestTasks.WriteExampleApp.ID)
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
