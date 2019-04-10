package com.testwithspring.master.kotlin

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.params.provider.CsvSource

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag

@Tag("unitTest")
@DisplayName("@CsvFileSource example")
internal class CsvFileSourceExampleTest {

    @DisplayName("Should pass the method parameters provided by the test-data.csv file")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @CsvFileSource(resources = ["/test-data.csv"])
    fun sum(a: Int, b: Int, sum: Int) {
        assertEquals(sum, a + b)
    }
}