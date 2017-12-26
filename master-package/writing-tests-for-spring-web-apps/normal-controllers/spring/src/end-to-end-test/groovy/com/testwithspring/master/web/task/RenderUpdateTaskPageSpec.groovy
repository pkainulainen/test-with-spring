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
class RenderUpdateTaskPageSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver browser

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
