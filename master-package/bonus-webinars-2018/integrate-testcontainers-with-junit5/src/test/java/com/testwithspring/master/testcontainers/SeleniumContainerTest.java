package com.testwithspring.master.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Run a Selenium WebDriver container")
public class SeleniumContainerTest {

    private static BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
                    .withDesiredCapabilities(DesiredCapabilities.chrome());

    @BeforeAll
    static void startContainer() {
        chrome.start();
    }

    @Test
    @DisplayName("Should return the correct title of the course's website")
    public void shouldReturnCorrectTitleOfCoursesWebsite() {
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
