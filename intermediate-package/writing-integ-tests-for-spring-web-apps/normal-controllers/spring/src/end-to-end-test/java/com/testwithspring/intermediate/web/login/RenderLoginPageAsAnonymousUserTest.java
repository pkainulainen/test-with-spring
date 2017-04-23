package com.testwithspring.intermediate.web.login;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import com.testwithspring.intermediate.SeleniumTestRunner;
import com.testwithspring.intermediate.SeleniumWebDriver;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SeleniumTestRunner.class)
@SeleniumTest(driver = ChromeDriver.class)
@Category(EndToEndTest.class)
public class RenderLoginPageAsAnonymousUserTest {

    @SeleniumWebDriver
    private WebDriver browser;

    private LoginPage loginPage;

    @Before
    public void createLoginPage() {
        loginPage = new LoginPage(browser);
    }

    @Test
    public void shouldOpenLoginPage() {
        LoginPage shownPage = loginPage.open();
        assertThat(shownPage.isOpen()).isTrue();
    }

    @Test
    public void shouldOpenLoginPageWithVisibleLoginForm() {
        LoginPage shownPage = loginPage.open();

        assertThat(shownPage.isLoginFormVisible()).isTrue();
    }

    @Test
    public void shouldOpenLoginPageWithEmptyLoginForm() {
        LoginPage shownPage = loginPage.open();

        assertThat(shownPage.getEmailAddress()).isEmpty();
        assertThat(shownPage.getPassword()).isEmpty();
    }

    @Test
    public void shouldOpenLoginWithoutVisibleAuthenticatedUserError() {
        LoginPage shownPage = loginPage.open();

        assertThat(shownPage.isAuthenticatedUserErrorVisible()).isFalse();
    }
}
