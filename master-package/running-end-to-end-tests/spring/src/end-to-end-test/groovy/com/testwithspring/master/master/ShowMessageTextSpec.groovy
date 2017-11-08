package com.testwithspring.master.master

import com.testwithspring.master.EndToEndTest
import org.junit.experimental.categories.Category
import org.openqa.selenium.chrome.ChromeDriver
import spock.lang.Specification

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
