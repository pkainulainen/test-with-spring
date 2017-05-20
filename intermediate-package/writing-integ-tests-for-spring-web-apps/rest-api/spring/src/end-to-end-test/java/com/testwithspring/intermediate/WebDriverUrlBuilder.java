package com.testwithspring.intermediate;

/**
 * Provides a factory method that is used to create
 * absolute URL addresses.
 */
public final class WebDriverUrlBuilder {

    private WebDriverUrlBuilder() {}

    /**
     * Builds an absolute URL address by appending the provided path to
     * the base URL.
     *
     * @param path      The path. The value of this parameter can be either a
     *                  static {@code String} or a format {@code String} that uses
     *                  the format supported by the {@link String#format(String, Object...)} method.
     * @params params   The parameters that are added to the path if the path
     *                  uses the format supported by the {@link String#format(String, Object...)} method.
     * @throws IllegalArgumentException If relative url is null or empty.
     * @return
     */
    public static String buildFromPath(String path, Object... params) {
        if ((path == null) || path.isEmpty()) {
            throw new IllegalArgumentException("Cannot build an absolute URL because the path is null or empty.");
        }

        path = String.format(path, params);

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
