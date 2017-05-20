package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class PlaceholderTest {

    @SeleniumWebDriver
    private WebDriver browser;

    @Test
    public void openFrontPage() {
        browser.get(WebDriverUrlBuilder.buildFromPath("/"));
    }
}
