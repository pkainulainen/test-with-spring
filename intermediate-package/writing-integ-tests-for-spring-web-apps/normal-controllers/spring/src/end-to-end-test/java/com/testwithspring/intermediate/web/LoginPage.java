package com.testwithspring.intermediate.web;


import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This page object is used to interact with the login page.
 */
final class LoginPage {

    private static final String EMAIL_ADDRESS_INPUT_ID = "email-address";
    private static final String LOGIN_FORM_ID = "login-form";
    private static final String LOGOUT_FORM_ID = "logout-form";
    private static final String PASSWORD_INPUT_ID = "password";

    private final WebDriver browser;

    LoginPage(WebDriver browser) {
        this.browser = browser;
    }

    /**
     * Opens the login page.
     */
    void open() {
        String loginPageUrl = WebDriverUrlBuilder.buildFromRelativeUrl("user/login");
        browser.get(loginPageUrl);
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
     * Logs the user in by using the provided email address and password.
     * @param emailAddress
     * @param password
     */
    void login(String emailAddress, String password) {
        typeEmailAddress(emailAddress);
        typePassword(password);
        submitLoginForm();
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
     */
    void logout() {
        if (!browser.findElements(By.id(LOGOUT_FORM_ID)).isEmpty()) {
            browser.findElement(By.id(LOGOUT_FORM_ID)).submit();
        }
    }
}
