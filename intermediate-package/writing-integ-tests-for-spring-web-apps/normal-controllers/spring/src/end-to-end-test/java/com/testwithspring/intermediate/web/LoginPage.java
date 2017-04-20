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
}
