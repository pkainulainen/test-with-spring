package com.testwithspring.intermediate.web.login;


import com.testwithspring.intermediate.SeleniumWait;
import com.testwithspring.intermediate.WebDriverUrlBuilder;
import com.testwithspring.intermediate.web.task.TaskListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This page object is used to interact with the login page.
 */
public final class LoginPage {

    private static final String AUTHENTICATED_USER_ERROR_ID = "authenticated-user-error";
    private static final String EMAIL_ADDRESS_INPUT_ID = "email-address";
    private static final String LOGIN_ERROR_ALERT_ID = "login-error-alert";
    private static final String LOGIN_FORM_ID = "login-form";
    private static final String LOGIN_SUBMIT_BUTTON_ID = "login-submit-button";
    private static final String PASSWORD_INPUT_ID = "password";

    private final WebDriver browser;
    private final String pageUrl;

    public LoginPage(WebDriver browser) {
        this.browser = browser;
        this.pageUrl = WebDriverUrlBuilder.buildFromPath("/user/login");
    }

    /**
     * Opens the login page as anonymous user.
     * @return The page object that symbolizes the opened page.
     */
    public LoginPage openAsAnonymousUser() {
        browser.get(pageUrl);
        waitUntilPageIsOpen();

        return new LoginPage(browser);
    }

    /**
     * Opens the login page as authenticated user.
     * @return The page object that symbolizes the opened page.
     */
    public LoginPage openAsAuthenticatedUser() {
        browser.get(pageUrl);

        SeleniumWait.waitUntilElementIsVisible(browser, By.id(AUTHENTICATED_USER_ERROR_ID));

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
     * Returns true if the authenticated user error is visible on the page and false otherwise.
     * @return
     */
    boolean isAuthenticatedUserErrorVisible() {
        return !browser.findElements(By.id(AUTHENTICATED_USER_ERROR_ID)).isEmpty();
    }

    /**
     * Returns true if the login error alert is visible on the page and false otherwise.
     * @return
     */
    boolean isLoginAlertVisible() {
        return !browser.findElements(By.id(LOGIN_ERROR_ALERT_ID)).isEmpty();
    }

    /**
     * Returns true if login form is not visible and false otherwise.
     * @return
     */
    boolean isLoginFormVisible() {
        return !browser.findElements(By.id(LOGIN_FORM_ID)).isEmpty();
    }

    /**
     * @return true if the login page is open and false otherwise.
     */
    boolean isOpen() {
        return browser.getCurrentUrl().equals(pageUrl);
    }

    /**
     * Logs the user in by using the provided email address and password. This
     * method expects that the provided email address and password are correct.
     * @param emailAddress
     * @param password
     * @return The page object that symbolizes the task list page.
     */
    public TaskListPage login(String emailAddress, String password) {
        typeEmailAddress(emailAddress);
        typePassword(password);
        submitLoginForm();

        TaskListPage shown = new TaskListPage(browser);
        shown.waitUntilPageIsOpen();

        return shown;
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

        SeleniumWait.waitUntilElementIsVisible(browser, By.id(LOGIN_ERROR_ALERT_ID));

        return new LoginPage(browser);
    }

    public void waitUntilPageIsOpen() {
        SeleniumWait.waitUntilElementIsClickable(browser, By.id(LOGIN_SUBMIT_BUTTON_ID));
    }

    private void typeEmailAddress(String emailAddress) {
        browser.findElement(By.id(EMAIL_ADDRESS_INPUT_ID)).sendKeys(emailAddress);
    }

    private void typePassword(String password) {
        browser.findElement(By.id(PASSWORD_INPUT_ID)).sendKeys(password);
    }

    private void submitLoginForm() {
        browser.findElement(By.id(LOGIN_SUBMIT_BUTTON_ID)).click();
    }
}
