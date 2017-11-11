package com.testwithspring.master.web.login

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.NavigationBar
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.TaskTrackerApplication
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
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
class RenderLoginPageSpec extends Specification {

    @Autowired
    WebDriver browser

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
