package com.testwithspring.master.message


import com.testwithspring.master.EndToEndTest
import org.junit.experimental.categories.Category
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment

@SpringBootTest(classes = MessageApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@Category(EndToEndTest.class)
class ShowMessageTextSpec extends Specification {

    def browser = new ChromeDriver()

    def 'Open message page'() {

        when: 'A user opens the message'
        browser.get('http://localhost:8080')

        then: 'Should show the correct title'
        browser.title == 'Hello World!'
    }

    def cleanup() {
        browser.quit()
    }
}
