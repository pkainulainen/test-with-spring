package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.web.login.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * This page is used to interact with the navigation bar.
 */
public final class NavigationBar {

    private static final String LOGOUT_BUTTON_ID = "log-out-button";

    private final WebDriver browser;

    public NavigationBar(WebDriver browser) {
        this.browser = browser;
    }

    /**
     * Logs the current user out.
     * @return the next page that is rendered when user is logged out (login page).
     * @throws RuntimeException if logout button is not visible.
     */
    public LoginPage logUserOut() {
        if (browser.findElements(By.id(LOGOUT_BUTTON_ID)).isEmpty()) {
            throw new RuntimeException("Cannot log the user out because the logout button is not visible. Is user logged in?");
        }

        browser.findElement(By.id(LOGOUT_BUTTON_ID)).click();

        LoginPage shown = new LoginPage(browser);
        shown.waitUntilPageIsOpen();
        return shown;
    }
}
