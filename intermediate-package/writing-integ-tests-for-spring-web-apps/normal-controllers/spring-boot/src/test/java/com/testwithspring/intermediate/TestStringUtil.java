package com.testwithspring.intermediate;

public final class TestStringUtil {

    /**
     * Prevents instantiation.
     */
    private TestStringUtil() {}

    /**
     * Creates a new {@code String} object.
     * @param length    The length of the created {@code String}.
     * @return  The created {@code String} object.s
     */
    public static String createStringWithLength(int length) {
        StringBuilder string = new StringBuilder();
        for (int index = 0; index < length; index++) {
            string.append("a");
        }
        return string.toString();
    }
}
