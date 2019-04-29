package com.testwithspring.master.kotlin.arguments

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.params.provider.CsvSource

@Tag("unitTest")
@DisplayName("@CsvFileSource example")
internal class CsvFileSourceExampleTest {

    @DisplayName("Should pass the function parameters provided by the test-data.csv file")
    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}")
    @CsvFileSource(resources = ["/test-data.csv"])
    fun sum(a: Int, b: Int, sum: Int) {
        assertThat(a + b).isEqualByComparingTo(sum)
    }
}