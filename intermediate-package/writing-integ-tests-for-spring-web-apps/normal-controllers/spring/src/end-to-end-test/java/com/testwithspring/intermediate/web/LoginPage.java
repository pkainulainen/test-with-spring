package com.testwithspring.intermediate.web;


import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

/**
 * This page object is used to interact with the login page.
 */
final class LoginPage {

    private static final String EMAIL_ADDRESS_INPUT_ID = "email-address";
    private static final String LOGIN_ERROR_ALERT_ID = "login-error-alert";
    private static final String LOGIN_FORM_ID = "login-form";
    private static final String LOGOUT_FORM_ID = "logout-form";
    private static final String PASSWORD_INPUT_ID = "password";

    private final WebDriver browser;
    private final String pageUrl;

    LoginPage(WebDriver browser) {
        this.browser = browser;
        this.pageUrl = WebDriverUrlBuilder.buildFromRelativeUrl("user/login");
    }

    /**
     * Opens the login page.
     * @return The page object that symbolizes the opened page.
     */
    LoginPage open() {
        browser.get(pageUrl);
        return new LoginPage(browser);
    }

    /**
     * Returns the email address that is entered to the login form.
     * @return
     */
    String getEmailAddress() {
        return browser.findElement(By.id(EMAIL_ADDRESS_INPUT_ID)).getAttribute("value");
    }

    /**
     * Returns the password that is entered to the login form.
     * @return
     */
    String getPassword() {
        return browser.findElement(By.id(PASSWORD_INPUT_ID)).getAttribute("value");
    }

    /**
     * Returns the url of the login page.
     * @return
     */
    String getPageUrl() {
        return pageUrl;
    }

    /**
     * Returns the login page url that is used after a failed login.
     * @return
     */
    String getLoginFailedPageUrl() {
        return pageUrl + "?error=bad_credentials";
    }

    /**
     * Returns true if the login error alert is visible on the page and false otherwise.
     * @return
     */
    boolean isLoginAlertVisible() {
        return !browser.findElements(By.id(LOGIN_ERROR_ALERT_ID)).isEmpty();
    }

    /**
     * Logs the user in by using the provided email address and password. This
     * method expects that the provided email address and password are correct.
     * @param emailAddress
     * @param password
     * @return The page object that symbolizes the task list page.
     */
    TaskListPage login(String emailAddress, String password) {
        typeEmailAddress(emailAddress);
        typePassword(password);
        submitLoginForm();

        return new TaskListPage(browser);
    }

    /**
     * Logs user in by using the provided email address and password. This method
     * expects that the provided email address and/or password are not correct.
     * @param emailAddress
     * @param password
     * @return The page object that symbolizes the login page.
     */
    LoginPage loginAndExpectFailure(String emailAddress, String password) {
        typeEmailAddress(emailAddress);
        typePassword(password);
        submitLoginForm();

        return new LoginPage(browser);
    }

    private void typeEmailAddress(String emailAddress) {
        browser.findElement(By.id(EMAIL_ADDRESS_INPUT_ID)).sendKeys(emailAddress);
    }

    private void typePassword(String password) {
        browser.findElement(By.id(PASSWORD_INPUT_ID)).sendKeys(password);
    }

    private void submitLoginForm() {
        browser.findElement(By.id(LOGIN_FORM_ID)).submit();
    }

    /**
     * Logs the user out if the user has been logged in. If the user
     * is not logged in, this method doesn't do anything.
     *
     * Even though the login button is visible on every page, I added
     * this method to this class because this way we can manage the
     * logged in user by using a single page object.
     */
     Optional<LoginPage> logout() {
        if (!browser.findElements(By.id(LOGOUT_FORM_ID)).isEmpty()) {
            browser.findElement(By.id(LOGOUT_FORM_ID)).submit();
            return Optional.of(new LoginPage(browser));
        }

        return Optional.empty();
    }
}
