package com.testwithspring.master.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This example demonstrates how we can run a Selenium WebDriver
 * container with JUnit 5 by using the "manual approach".
 */
@DisplayName("Run a Selenium WebDriver container")
class SeleniumContainerTest {

    private static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
                    .withDesiredCapabilities(DesiredCapabilities.chrome());

    @BeforeAll
    static void startContainer() {
        chrome.start();
    }

    @Test
    @DisplayName("Should return the correct title of the course's website")
    void shouldReturnCorrectTitleOfCoursesWebsite() {
        RemoteWebDriver driver = chrome.getWebDriver();
        driver.get("https://www.testwithspring.com");

        String title = driver.getTitle();
        assertThat(title).isEqualTo("Test With Spring Course — Save Time by Writing Less Test Code");
    }

    @AfterAll
    static void stopContainer() {
        chrome.stop();
    }
}
