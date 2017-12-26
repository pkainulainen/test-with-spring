package com.testwithspring.master.web.task

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.EndToEndTestTasks
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.SeleniumWebDriver
import com.testwithspring.master.web.NavigationBar
import com.testwithspring.master.web.login.LoginPage
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import spock.lang.Shared
import spock.lang.Specification

import static com.testwithspring.master.EndToEndTestUsers.JohnDoe
import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.not

@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
class DeleteTaskSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver browser

    TaskPage viewTaskPage

    def setup() {
        viewTaskPage = new TaskPage(browser, EndToEndTestTasks.DeleteMe.ID)
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

        /**
         * Because we initialize our database into a known state when
         * our web application is started, and we cannot know the order in
         * which our end-to-end are run, we cannot know the exact number
         * of tasks that are shown on the task list page.
         *
         * Because our SQL script inserts three rows into the tasks table,
         * our database as always at least two tasks. This means that the
         * task list page always shows at least two tasks.
         */
        and: 'Should display task list that has at least two tasks'
        def taskList = shownPage.getListItems()
        taskList.size() >= 2

        and: 'Should not display the deleted task'
        List<Long> shownTaskIds = []
        taskList.each {t ->
            shownTaskIds.add(t.id)
        }
        shownTaskIds not(contains(EndToEndTestTasks.DeleteMe.ID))
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
