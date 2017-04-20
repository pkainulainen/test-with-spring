package com.testwithspring.intermediate.web;


import com.testwithspring.intermediate.WebDriverUrlBuilder;
import org.openqa.selenium.WebDriver;

final class LoginPage {

    private final WebDriver browser;

    LoginPage(WebDriver browser) {
        this.browser = browser;
    }

    void open() {
        String loginPageUrl = WebDriverUrlBuilder.buildFromRelativeUrl("user/login");
        browser.get(loginPageUrl);
    }
}
