package com.testwithspring.intermediate.example;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

/**
 * This test class demonstrates how we can open a web page
 * by using the {@code WebDriver.get()} method.
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
public class RenderClickSourcePageTest {

    @Autowired
    private WebDriver browser;

    @Test
    public void shouldOpenClickSourcePage() {
        browser.get("http://localhost:8080/click-source");
        assertThat(browser.getTitle()).isEqualTo("Click Source Page");
    }
}
