package com.testwithspring.intermediate.example;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import com.testwithspring.intermediate.SeleniumTestRunner;
import com.testwithspring.intermediate.SeleniumWebDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class demonstrates how we can click
 * a link by using the {@code WebElement.click()} method.
 */
@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class ClickTargetLinkTest {

    @SeleniumWebDriver
    private WebDriver browser;

    @Before
    public void openClickSourcePage() {
        browser.get("http://localhost:8080/click-source");
    }

    @Test
    public void shouldOpenClickTargetPage() {
        WebElement targetLink = browser.findElement(By.id("click-target-link"));
        targetLink.click();

        //Because the click() method doesn't wait for the next page to load,
        //we have to make sure that the page is loaded before we can invoke
        //our assertion.
        WebDriverWait wait = new WebDriverWait(browser, 3);
        wait.until(ExpectedConditions.urlToBe("http://localhost:8080/click-target"));

        assertThat(browser.getTitle()).isEqualTo("Click Target Page");
    }
}
