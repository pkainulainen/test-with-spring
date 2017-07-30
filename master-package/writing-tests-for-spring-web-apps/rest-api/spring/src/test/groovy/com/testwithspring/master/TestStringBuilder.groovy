package com.testwithspring.master

final class TestStringBuilder {

    /**
     * Creates a new {@code String} object.
     * @param length    The length of the created {@code String}.
     * @return  The created {@code String} object.
     */
    static def createStringWithLength(int length) {
        def string = new StringBuilder()
        length.times {
            string << 'a'
        }
        return string.toString()
    }
}
