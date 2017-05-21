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

    /**
     * Returns true if the tested single page application uses the HTML 5
     * mode and false otherwise. This methods reads the returned value from
     * the {@code webdriver.html5.mode.enabled} system property.
     * @return
     */
    public static boolean isHtml5ModeEnabled() {
        String html5ModeEnabled = System.getProperty("webdriver.html5.mode.enabled");
        if (html5ModeEnabled == null) {
            throw new RuntimeException("Cannot determine if HTML5 mode is enabled. Set the value of the webdriver.html5.mode.enabled system property");
        }
        return Boolean.valueOf(html5ModeEnabled);
    }
}
