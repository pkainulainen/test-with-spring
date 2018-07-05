package com.testwithspring.master.testcontainers;

import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class demonstrates how you can run containers that include
 * web browsers and write tests with Selenium.
 */
public class SeleniumContainerTest {

    @ClassRule
    public static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome());

    @Test
    public void shouldReturnCorrectTitleOfCoursesWebsite() {
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("https://www.testwithspring.com");

        String title = driver.getTitle();
        assertThat(title).isEqualTo("Test With Spring Course — Save Time by Writing Less Test Code");
    }
}
