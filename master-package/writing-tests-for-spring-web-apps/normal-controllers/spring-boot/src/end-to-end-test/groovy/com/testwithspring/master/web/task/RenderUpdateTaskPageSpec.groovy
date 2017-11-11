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
class RenderUpdateTaskPageSpec extends Specification {

    @Autowired
    WebDriver browser

    UpdateTaskPage updateTaskPage

    def setup() {
        updateTaskPage = new UpdateTaskPage(browser, EndToEndTestTasks.WriteLesson.ID)
    }

    def 'Open update task task as a registered user'() {

        UpdateTaskPage shownPage

        given: 'A user is authenticated successfully'
        LoginPage loginPage = new LoginPage(browser).open()
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'A user opens the update task page'
        shownPage = updateTaskPage.open()

        then: 'Should open the update task page'
        shownPage.isOpen()

        and: 'Should display the update task form with the correct title'
        shownPage.getForm().getTaskTitle() == EndToEndTestTasks.WriteLesson.TITLE

        and: 'Should display the update task form with the correct description'
        shownPage.getForm().getTaskDescription() == EndToEndTestTasks.WriteLesson.DESCRIPTION
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
