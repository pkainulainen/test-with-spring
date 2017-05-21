package com.testwithspring.intermediate.web.login;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import com.testwithspring.intermediate.SeleniumTestRunner;
import com.testwithspring.intermediate.SeleniumWebDriver;
import com.testwithspring.intermediate.web.NavigationBar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.testwithspring.intermediate.EndToEndTestUsers.JohnDoe;
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
        loginPage = new LoginPage(browser).openAsAnonymousUser();
        loginPage.login(JohnDoe.EMAIL_ADDRESS, JohnDoe.PASSWORD);
    }

    @Test
    public void shouldOpenLoginPage() {
        LoginPage shownPage = loginPage.openAsAuthenticatedUser();
        assertThat(shownPage.isOpen()).isTrue();
    }

    @Test
    public void shouldOpenLoginPageWithoutVisibleLoginForm() {
        LoginPage shownPage = loginPage.openAsAuthenticatedUser();

        assertThat(shownPage.isLoginFormVisible()).isFalse();
    }

    @Test
    public void shouldOpenLoginWithVisibleAuthenticatedUserError() {
        LoginPage shownPage = loginPage.openAsAuthenticatedUser();

        assertThat(shownPage.isAuthenticatedUserErrorVisible()).isTrue();
    }

    @After
    public void logoutUser() {
        new NavigationBar(browser).logUserOut();
    }
}
