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
import org.openqa.selenium.support.ui.Select;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class demonstrates how we can
 * test that a form page is rendered correctly.
 */
@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderFormPageTest {

    @SeleniumWebDriver
    private WebDriver browser;

    @Before
    public void openFormPage() {
        browser.get("http://localhost:8080/form");
    }

    @Test
    public void shouldRenderFormPageWithCorrectTitle() {
        assertThat(browser.getTitle()).isEqualTo("Example Form Page");
    }

    @Test
    public void shouldRenderFormPageWithCorrectHeader() {
        assertThat(browser.findElement(By.id("page-header")).getText()).isEqualTo("Example Form Page");
    }

    @Test
    public void shouldRenderFormPageWithEmptyForm() {
        WebElement messageInput = browser.findElement(By.id("message"));
        assertThat(messageInput.getAttribute("value")).isEmpty();

        Select numberSelect = new Select(browser.findElement(By.id("number")));
        assertThat(numberSelect.getFirstSelectedOption().getAttribute("value")).isEqualTo("1");

        WebElement checkbox = browser.findElement(By.id("checkbox"));
        assertThat(checkbox.isSelected()).isFalse();

        WebElement radioButton = browser.findElement(By.id("radio-button"));
        assertThat(radioButton.isSelected()).isFalse();
    }
}
