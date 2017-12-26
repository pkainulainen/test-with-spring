package com.testwithspring.master.web.login

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.SeleniumWebDriver
import com.testwithspring.master.web.NavigationBar
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import spock.lang.Shared
import spock.lang.Specification

import static com.testwithspring.master.EndToEndTestUsers.*

@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
class RenderLoginPageSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver browser

    LoginPage loginPage

    def setup() {
        loginPage = new LoginPage(browser)
    }

    def 'Open login page as an anonymous user'() {

        LoginPage shownPage

        when: 'An anonymous user opens the login page'
        shownPage = loginPage.open()

        then: 'Should open the login page'
        shownPage.isOpen()

        and: 'Should display the login form'
        shownPage.isLoginFormVisible()

        and: 'Should display an empty login form'
        shownPage.getEmailAddress().isEmpty()
        shownPage.getPassword().isEmpty()

        and: 'Should not display the error shown to authenticated users'
        !shownPage.isAuthenticatedUserErrorVisible()
    }

    def 'Open login page as an authenticated user'() {

        LoginPage shownPage

        given: 'The user is authenticated successfully'
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD)

        when: 'The user opens the login page'
        shownPage = loginPage.open()

        then: 'Should open the login page'
        shownPage.isOpen()

        and: 'Should not display the login form'
        !shownPage.isLoginFormVisible()

        and: 'Should display the error shown to authenticated users'
        shownPage.isAuthenticatedUserErrorVisible()

        cleanup:
        new NavigationBar(browser).logUserOut()
    }
}
