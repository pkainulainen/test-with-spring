package com.testwithspring.intermediate.web.login;

import com.testwithspring.intermediate.EndToEndTest;
import com.testwithspring.intermediate.SeleniumTest;
import com.testwithspring.intermediate.TaskTrackerApplication;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Before;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TaskTrackerApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
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
public class RenderLoginPageAsAnonymousUserTest {

    @Autowired
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
