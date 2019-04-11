package com.testwithspring.master.kotlin

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag

@Tag("unitTest")
@DisplayName("@CsvSource example")
internal class CsvSourceExampleTest {

    @DisplayName("Should pass the function parameters provided by the @CsvSource annotation")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @CsvSource("1, 1, 2", "2, 3, 5")
    fun sum(a: Int, b: Int, sum: Int) {
        assertEquals(sum, a + b)
    }
}