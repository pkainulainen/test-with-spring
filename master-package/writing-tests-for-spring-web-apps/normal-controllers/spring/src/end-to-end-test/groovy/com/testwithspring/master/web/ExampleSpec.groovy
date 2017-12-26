package com.testwithspring.master.web

import com.testwithspring.master.EndToEndTest
import com.testwithspring.master.SeleniumTest
import com.testwithspring.master.SeleniumWebDriver
import org.junit.experimental.categories.Category
import org.openqa.selenium.WebDriver
import spock.lang.Shared
import spock.lang.Specification

@SeleniumTest
@Category(EndToEndTest.class)
class ExampleSpec extends Specification {

    @SeleniumWebDriver
    @Shared WebDriver webDriver

    def 'Do nothing'() {
        expect:
        true
    }
}
