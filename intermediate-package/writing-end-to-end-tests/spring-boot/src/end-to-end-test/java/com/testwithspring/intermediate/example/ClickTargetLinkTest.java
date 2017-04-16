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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

/**
 * This test class demonstrates how we can click
 * a link by using the {@code WebElement.click()} method.
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
public class ClickTargetLinkTest {

    @Autowired
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
