package com.testwithspring.intermediate;

/**
 * Provides static methods that are used to access environment
 * specific configuration.
 */
public final class WebDriverEnvironment {

    private WebDriverEnvironment() {}

    /**
     * Returns the base url of the tested web application. The
     * base url is used to access the front page of the web
     * application. Its value is read from the {@code webdriver.base.url}
     * system property.
     *
     * @return
     * @throws RuntimeException if the base url is not found.
     */
    public static String getBaseUrl() {
        String baseUrl = System.getProperty("webdriver.base.url");
        if (baseUrl == null) {
            throw new RuntimeException("No base url found! Set the value of the webdriver.base.url system property.");
        }
        return baseUrl;
    }
}
