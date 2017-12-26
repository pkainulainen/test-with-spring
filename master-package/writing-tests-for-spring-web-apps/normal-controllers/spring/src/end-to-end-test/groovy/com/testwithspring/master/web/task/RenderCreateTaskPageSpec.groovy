package com.testwithspring.master.web.task

import com.testwithspring.master.EndToEndTest
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
class RenderCreateTaskPageSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver browser

    CreateTaskPage createTaskPage

    def setup() {
        createTaskPage = new CreateTaskPage(browser)
    }

    def 'Open create task page as a registered user'() {

        CreateTaskPage shownPage

        given: 'A user is authenticated successfully'
        LoginPage loginPage = new LoginPage(browser).open()
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'A User opens the create task page'
        shownPage = createTaskPage.open()

        then: 'Should open the create task page'
        shownPage.isOpen()

        and: 'Should display the create task form with empty title'
        shownPage.getForm().getTaskTitle().isEmpty()

        and: 'Should display the create task form with empty description'
        shownPage.getForm().getTaskDescription().isEmpty()
    }

    def cleanup() {
        new NavigationBar(browser).logUserOut()
    }
}
