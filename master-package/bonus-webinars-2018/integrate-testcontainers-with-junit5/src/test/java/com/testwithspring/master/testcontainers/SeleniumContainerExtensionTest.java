package com.testwithspring.master.testcontainers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This example demonstrates how we can run a Selenium WebDriver
 * container with JUnit 5 by using a custom JUnit 5 extension.
 */
@ExtendWith(TestContainersExtension.class)
@DisplayName("Run a Selenium WebDriver container")
class SeleniumContainerExtensionTest {

    private static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
                    .withDesiredCapabilities(DesiredCapabilities.chrome());

    @Test
    @DisplayName("Should return the correct title of the course's website")
    void shouldReturnCorrectTitleOfCoursesWebsite() {
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("https://www.testwithspring.com");

        String title = driver.getTitle();
        assertThat(title).isEqualTo("Test With Spring Course — Save Time by Writing Less Test Code");
    }
}
