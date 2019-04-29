package com.testwithspring.master.kotlin.conversion

/**
 * This class provides a static factory function that's used
 * by JUnit 5 when it provides the function parameters
 * of our parameterized test function.
 */
class Book {

    companion object {

        @JvmStatic
        fun fromTitle(title: String): Book {
            return Book().apply {
                this.title = title
            }
        }
    }

    lateinit var title: String
}