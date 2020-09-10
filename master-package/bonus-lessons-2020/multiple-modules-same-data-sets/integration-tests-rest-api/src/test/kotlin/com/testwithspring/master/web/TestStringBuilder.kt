package com.testwithspring.master.web

/**
 * Provides one utility method that creates new {@code String}
 * objects. This method is useful when we are writing unit tests
 * for Spring MVC controllers which have validation rules that
 * validate the length of a Json object's attribute.
 */
class TestStringBuilder private constructor() {

    companion object {

        /**
         * Creates a new {@code String} object.
         * @param   length  The length of the created {@code String} object.
         * @return  The created object.
         */
        fun createStringWithLength(length: Int): String = "A".repeat(length)
    }
}