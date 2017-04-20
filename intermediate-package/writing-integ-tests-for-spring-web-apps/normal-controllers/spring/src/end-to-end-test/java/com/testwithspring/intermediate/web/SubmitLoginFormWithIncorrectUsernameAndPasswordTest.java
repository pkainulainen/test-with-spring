package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.testwithspring.intermediate.EndToEndTestUsers.*;
import static com.testwithspring.intermediate.EndToEndTestUsers.JohnDoe;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class SubmitLoginFormWithIncorrectUsernameAndPasswordTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private LoginPage loginPage;

    @Before
    public void openLoginPage() {
        loginPage = new LoginPage(browser);
        loginPage.open();
    }

    @Test
    public void shouldRenderLoginPage() {
        LoginPage shownPage = loginPage.loginAndExpectFailure(UnknownUser.EMAIL_ADDRESS, UnknownUser.PASSWORD);

        String loginPageUrl = shownPage.getLoginFailedPageUrl();
        assertThat(browser.getCurrentUrl()).isEqualTo(loginPageUrl);
    }

    @Test
    public void shouldShowLoginErrorAlert() {
        LoginPage shownPage = loginPage.loginAndExpectFailure(UnknownUser.EMAIL_ADDRESS, UnknownUser.PASSWORD);
        assertThat(shownPage.isLoginAlertVisible()).isTrue();
    }
}
