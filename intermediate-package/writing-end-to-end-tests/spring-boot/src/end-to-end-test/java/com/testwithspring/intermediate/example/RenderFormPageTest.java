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
 * This test class demonstrates how we can
 * test that a form page is rendered correctly.
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
public class RenderFormPageTest {

    @Autowired
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
        assertThat(numberSelect.getAllSelectedOptions()).isEmpty();

        WebElement checkbox = browser.findElement(By.id("checkbox"));
        assertThat(checkbox.isSelected()).isFalse();

        WebElement radioButton = browser.findElement(By.id("radio-button"));
        assertThat(radioButton.isSelected()).isFalse();
    }
}
