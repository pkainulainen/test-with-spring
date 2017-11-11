package com.testwithspring.master.web.login

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.NavigationBar
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.TaskTrackerApplication
import com.testwithspring.master.config.Profiles
import com.testwithspring.master.web.task.TaskListPage
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
class LogUserInSpec extends Specification {

    @Autowired
    WebDriver browser

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
