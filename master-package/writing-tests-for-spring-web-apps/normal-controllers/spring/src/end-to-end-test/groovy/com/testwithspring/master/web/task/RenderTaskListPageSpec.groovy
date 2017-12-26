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

@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
class RenderTaskListPageSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver browser

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
        def tasks = shownPage.getListItems()
        tasks.size() > 2

        /**
         * Because we initialize our database into a known state when
         * our web application is started, and we cannot know the order in
         * which our end-to-end are run, we can write assertions only
         * for the task that is not updated by our other end-to-end tests.
         *
         * The first task that is shown on the task list page is not updated
         * by our other end-to-end tests. If we want verify that the task list
         * page shows the correct information of a task, we can verify that
         * first task of our task list is rendered correctly.
         */
        and: 'Should show the correct information of the first task'
        def firstTask = tasks.get(0)

        firstTask.id == EndToEndTestTasks.WriteExampleApp.ID
        firstTask.title == EndToEndTestTasks.WriteExampleApp.TITLE
        firstTask.status == EndToEndTestTasks.WriteExampleApp.STATUS

        /**
         * It's a good idea to verify that the links found from the tested
         * HTML page are working as expected. This test ensures that we
         * can navigate fromt he task list page to the view task page.
         */
        and: 'A user must be able to navigate to the view task page'
        def taskPage = firstTask.viewTask()
        taskPage.isOpen()
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
