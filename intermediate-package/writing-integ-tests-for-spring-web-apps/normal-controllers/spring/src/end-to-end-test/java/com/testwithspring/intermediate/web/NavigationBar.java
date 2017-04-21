package com.testwithspring.intermediate.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

/**
 * This page is used to interact with the navigation bar.
 */
final class NavigationBar {

    private static final String LOGOUT_FORM_ID = "logout-form";

    private final WebDriver browser;

    NavigationBar(WebDriver browser) {
        this.browser = browser;
    }

    /**
     * Logs the user out if the user has been logged in. If the user
     * is not logged in, this method doesn't do anything.
     */
    Optional<LoginPage> logout() {
        if (!browser.findElements(By.id(LOGOUT_FORM_ID)).isEmpty()) {
            browser.findElement(By.id(LOGOUT_FORM_ID)).submit();
            return Optional.of(new LoginPage(browser));
        }

        return Optional.empty();
    }
}
