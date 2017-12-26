package com.testwithspring.master.web.login

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.SeleniumWebDriver
import com.testwithspring.master.web.NavigationBar
import com.testwithspring.master.web.task.TaskListPage
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import spock.lang.Shared
import spock.lang.Specification

import static com.testwithspring.master.EndToEndTestUsers.*

@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
class LogUserInSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver browser

    LoginPage loginPage

    def setup() {
        loginPage = new LoginPage(browser)
    }

    def 'Log user in by using incorrect email address and password'() {

        LoginPage shownPage

        given: 'A user opens the login page'
        loginPage = loginPage.open()

        when: 'A user logs in by using incorrect email address and password'
        shownPage = loginPage.loginAndExpectFailure(UnknownUser.EMAIL_ADDRESS, UnknownUser.PASSWORD)

        then: 'Should open login page by using the login error url'
        shownPage.isOpenWithLoginErrorUrl()

        and: 'Should display login error alert'
        shownPage.isLoginAlertVisible()
    }

    def 'Log user in by using correct email address and password'() {

        TaskListPage shownPage

        given: 'A user opens the login page'
        loginPage = loginPage.open()

        when: 'A user logs in by using correct email address and password'
        shownPage = loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        then: 'Should render task list page'
        shownPage.isOpen()

        cleanup:
        new NavigationBar(browser).logUserOut()
    }
}
