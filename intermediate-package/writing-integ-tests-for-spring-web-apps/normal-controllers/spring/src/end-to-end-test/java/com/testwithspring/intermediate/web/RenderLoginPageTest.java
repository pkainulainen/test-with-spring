package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import com.testwithspring.intermediate.SeleniumTestRunner;
import com.testwithspring.intermediate.SeleniumWebDriver;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderLoginPageTest {

    @SeleniumWebDriver
    private WebDriver browser;

    @Test
    public void shouldOpenLoginPageWithCorrectTitle() {
        browser.get("http://localhost:8080/user/login");
        assertThat(browser.getTitle()).isEqualTo("Login");
    }
}
