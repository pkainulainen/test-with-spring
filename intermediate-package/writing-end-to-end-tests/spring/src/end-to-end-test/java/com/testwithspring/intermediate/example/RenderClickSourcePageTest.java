package com.testwithspring.intermediate.example;

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

/**
 * This test class demonstrates how we can open a web page
 * by using the {@code WebDriver.get()} method.
 */
@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderClickSourcePageTest {

    @SeleniumWebDriver
    private WebDriver browser;

    @Test
    public void shouldOpenClickSourcePage() {
        browser.get("http://localhost:8080/click-source");
        assertThat(browser.getTitle()).isEqualTo("Click Source Page");
    }
}
