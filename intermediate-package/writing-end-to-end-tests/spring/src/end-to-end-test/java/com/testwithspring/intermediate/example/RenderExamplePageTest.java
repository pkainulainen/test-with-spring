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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class demonstrates how we can find elements
 * from the loaded HTML document.
 */
@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderExamplePageTest {

    @SeleniumWebDriver
    private WebDriver browser;

    @Before
    public void openExamplePage() {
        browser.get("http://localhost:8080");
    }

    @Test
    public void shouldOpenPageWithCorrectTitle() {
        assertThat(browser.getTitle()).isEqualTo("Hello World!");
    }

    @Test
    public void shouldOpenPageWithCorrectQuoteOne() {
        WebElement quote = browser.findElement(By.tagName("blockquote"));
        assertThat(quote.getText()).isEqualTo("This is a quote.");
    }

    @Test
    public void shouldOpenPageWithCorrectHeader() {
        WebElement pageHeader = browser.findElement(By.id("page-header"));
        assertThat(pageHeader.getText()).isEqualTo("Hello World!");
    }

    @Test
    public void shouldOpenPageWithCorrectTextBlockOne() {
        WebElement textBlock = browser.findElement(By.className("text-block"));
        assertThat(textBlock.getText()).isEqualTo("Lorem ipsum");
    }

    @Test
    public void shouldOpenWithTextInputThatHasCorrectText() {
        WebElement textInput = browser.findElement(By.name("text-input"));
        assertThat(textInput.getAttribute("value")).isEqualTo("Random text");
    }

    @Test
    public void shouldOpenPageThatHasLinkToGoogle() {
        WebElement googleLink = browser.findElement(By.linkText("Go to Google"));
        assertThat(googleLink.getAttribute("href")).isEqualTo("http://www.google.com/");
    }

    @Test
    public void shouldOpenPageThatHasLinkToTestWithSpringCourse() {
        WebElement testWithSpringLink = browser.findElement(By.partialLinkText("Test With Spring"));
        assertThat(testWithSpringLink.getAttribute("href")).isEqualTo("https://www.testwithspring.com/");
    }

    @Test
    public void shouldOpenPageWithCorrectTextBlockTwo() {
        WebElement textBlock = browser.findElement(By.cssSelector(".text-block"));
        assertThat(textBlock.getText()).isEqualTo("Lorem ipsum");
    }

    @Test
    public void shouldOpenPageWithCorrectQuoteTwo() {
        WebElement quote = browser.findElement(By.xpath("/html/body/blockquote"));
        assertThat(quote.getText()).isEqualTo("This is a quote.");
    }
}
