package com.testwithspring.intermediate;

/**
 * Provides factory methods that are used to build url
 * addresses.
 */
public final class WebDriverUrlBuilder {

    private WebDriverUrlBuilder() {}

    /**
     * Builds a url address by appending the relative url to
     * the base url.
     * @param relativeUrl
     * @throws IllegalArgumentException If relative url is null or empty.
     * @return
     */
    public static String buildFromRelativeUrl(String relativeUrl) {
        if ((relativeUrl == null) || relativeUrl.isEmpty()) {
            throw new IllegalArgumentException("Cannot build url because relative url is null or empty.");
        }

        String baseUrl = WebDriverEnvironment.getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        if (relativeUrl.startsWith("/")) {
            relativeUrl = relativeUrl.replaceFirst("/", "");
        }

        return baseUrl + relativeUrl;
    }
}
