package com.testwithspring.intermediate.message;

import com.testwithspring.intermediate.EndToEndTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MessageApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@Category(EndToEndTest.class)
public class ShowMessageText {

    private WebDriver browser;

    @Before
    public void createBrowser() {
        browser = new ChromeDriver();
    }

    @Test
    public void shouldOpenFrontPage() {
        browser.get("http://localhost:8080");
        assertThat(browser.getTitle()).isEqualTo("Hello World!");
    }

    @After
    public void closeBrowser() {
        browser.quit();
    }
}
