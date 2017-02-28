package com.testwithspring.intermediate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.assertj.core.api.Assertions.assertThat;

@Category(EndToEndTest.class)
public class EndToEndPlaceHolderTest {

    private WebDriver browser;

    @Before
    public void createBrowser() {
        browser = new ChromeDriver();
    }

    @Test
    public void shouldOpenGoogleCom() {
        browser.get("http://www.google.com");
        assertThat(browser.getTitle()).isEqualTo("Google");
    }

    @After
    public void closeBrowser() {
        browser.quit();
    }
}
