package com.testwithspring.intermediate;

/**
 * Provides a factory method that is used to create
 * absolute URL addresses.
 */
public final class WebDriverUrlBuilder {

    private WebDriverUrlBuilder() {}

    /**
     * Builds an absolute URL address by appending the provided path to
     * the base url.
     * @param path
     * @throws IllegalArgumentException If relative url is null or empty.
     * @return
     */
    public static String buildFromPath(String path) {
        if ((path == null) || path.isEmpty()) {
            throw new IllegalArgumentException("Cannot build an absolute URL because the path is null or empty.");
        }

        String baseUrl = WebDriverEnvironment.getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        if (path.startsWith("/")) {
            path = path.replaceFirst("/", "");
        }

        return baseUrl + path;
    }
}
