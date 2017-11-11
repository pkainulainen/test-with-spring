package com.testwithspring.master.web

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.TaskTrackerApplication
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment

@SpringBootTest(classes = TaskTrackerApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles(Profiles.END_TO_END_TEST)
@Category(EndToEndTest.class)
class EndToEndPlaceholderSpec extends Specification {

    def browser = new ChromeDriver()

    def 'Open front page'() {

        when: 'An anonymous user opens the front page'
        browser.get('http://localhost:8080')

        then: 'Should render the login page'
        browser.currentUrl == 'http://localhost:8080/user/login'
    }

    def cleanup() {
        browser.quit()
    }
}
