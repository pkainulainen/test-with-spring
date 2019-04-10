package com.testwithspring.master.kotlin

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource

import java.util.stream.Stream

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag

@Tag("unitTest")
@DisplayName("@ArgumentsSource example")
class ArgumentsSourceExampleTest {

    @DisplayName("Should pass the method parameters provided by the CustomArgumentProvider class")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @ArgumentsSource(CustomArgumentProvider::class)
    fun sum(a: Int, b: Int, sum: Int) {
        assertEquals(sum, a + b)
    }

    internal class CustomArgumentProvider : ArgumentsProvider {

        @Throws(Exception::class)
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                    Arguments.of(1, 1, 2),
                    Arguments.of(2, 3, 5)
            )
        }
    }
}