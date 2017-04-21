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
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderLoginPageAsAuthenticatedUserTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private LoginPage loginPage;

    @Before
    public void logUserIn() {
        loginPage = new LoginPage(browser).open();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenLoginPage() {
        LoginPage shownPage = loginPage.open();

        String loginPageUrl = shownPage.getPageUrl();
        assertThat(browser.getCurrentUrl()).isEqualTo(loginPageUrl);
    }

    @Test
    public void shouldOpenLoginPageWithoutVisibleLoginForm() {
        LoginPage shownPage = loginPage.open();

        assertThat(shownPage.isLoginFormVisible()).isFalse();
    }

    @Test
    public void shouldOpenLoginWithVisibleAuthenticatedUserError() {
        LoginPage shownPage = loginPage.open();

        assertThat(shownPage.isAuthenticatedUserErrorVisible()).isTrue();
    }

    @After
    public void logoutUser() {
        new NavigationBar(browser).logout();
    }
}
