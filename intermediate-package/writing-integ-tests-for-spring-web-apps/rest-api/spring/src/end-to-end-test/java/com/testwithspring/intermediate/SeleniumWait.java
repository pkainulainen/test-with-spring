package com.testwithspring.intermediate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Provides static utility methods that are used to wait until a specific
 * condition is true.
 */
public final class SeleniumWait {

    private static final long DEFAULT_TIMEOUT_IN_SECONDS = 5;

    private SeleniumWait() {}

    /**
     * Waits as long as the specified element is clickable.
     * @param browser
     * @param elementLocator
     * @throws org.openqa.selenium.TimeoutException if the element is not clickable before the timeout is exceeded.
     */
    public static void waitUntilElementIsClickable(WebDriver browser, By elementLocator) {
        WebDriverWait wait = new WebDriverWait(browser, DEFAULT_TIMEOUT_IN_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(elementLocator));
    }

    /**
     * Waits as long as all elements found with the specified locator are visible.
     * @param browser
     * @param elementLocator
     * @throws org.openqa.selenium.TimeoutException if all elements are not visible before the timeout is exceeded.
     */
    public static void waitUntilAllElementsAreVisible(WebDriver browser, By elementLocator) {
        WebDriverWait wait = new WebDriverWait(browser, DEFAULT_TIMEOUT_IN_SECONDS);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(elementLocator));
    }

    /**
     * Waits as long as the specified element is visible.
     * @param browser
     * @param elementLocator
     * @throws org.openqa.selenium.TimeoutException if the element is not visible before the timeout is exceeded.
     */
    public static void waitUntilElementIsVisible(WebDriver browser, By elementLocator) {
        WebDriverWait wait = new WebDriverWait(browser, DEFAULT_TIMEOUT_IN_SECONDS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
    }
}
