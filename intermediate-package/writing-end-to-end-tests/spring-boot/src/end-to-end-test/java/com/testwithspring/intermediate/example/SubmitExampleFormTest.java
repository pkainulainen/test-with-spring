package com.testwithspring.intermediate.example;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

/**
 * This test class demonstrates how fill the form fields
 * of a form and submit that form.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExampleApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@SeleniumTest(driver = ChromeDriver.class)
@Sql(value = {
        "classpath:/com/testwithspring/intermediate/users.sql",
        "classpath:/com/testwithspring/intermediate/tasks.sql"
})
@Sql(
        value = "classpath:/com/testwithspring/intermediate/cleandb.sql",
        executionPhase = AFTER_TEST_METHOD
)
@ActiveProfiles(Profiles.END_TO_END_TEST)
@Category(EndToEndTest.class)
public class SubmitExampleFormTest {

    private static final String MESSAGE = "Hello world";
    private static final String NUMBER = "2";

    @Autowired
    private WebDriver browser;

    @Before
    public void openFormPage() {
        browser.get("http://localhost:8080/form");
    }

    @Test
    public void shouldRenderFormResultPageWithCorrectTitle() {
        fillAndSubmitForm();

        assertThat(browser.getTitle()).isEqualTo("Form Result Page");
    }

    @Test
    public void shouldRenderFormResultPageWithCorrectHeader() {
        fillAndSubmitForm();

        assertThat(browser.findElement(By.id("page-header")).getText()).isEqualTo("Form Result Page");
    }

    @Test
    public void shouldRenderFormPageWithEmptyForm() {
        fillAndSubmitForm();

        WebElement message = browser.findElement(By.id("form-message-value"));
        assertThat(message.getText()).isEqualTo(MESSAGE);

        WebElement number = browser.findElement(By.id("form-number-value"));
        assertThat(number.getText()).isEqualTo(NUMBER);

        WebElement checkbox = browser.findElement(By.id("form-checkbox-value"));
        assertThat(checkbox.getText()).isEqualTo("true");

        WebElement radioButton = browser.findElement(By.id("form-radio-button-value"));
        assertThat(radioButton.getText()).isEqualTo("true");
    }

    private void fillAndSubmitForm() {
        WebElement messageInput = browser.findElement(By.id("message"));
        messageInput.sendKeys(MESSAGE);

        Select numberSelect = new Select(browser.findElement(By.id("number")));
        numberSelect.selectByValue(NUMBER);

        WebElement checkbox = browser.findElement(By.id("checkbox"));
        checkbox.click();

        WebElement radioButton = browser.findElement(By.id("radio-button"));
        radioButton.click();

        browser.findElement(By.id("form")).submit();
    }
}
