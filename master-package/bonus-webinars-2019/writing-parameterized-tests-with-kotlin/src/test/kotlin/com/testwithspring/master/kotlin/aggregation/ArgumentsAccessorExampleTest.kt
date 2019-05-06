package com.testwithspring.master.kotlin.aggregation

import com.testspringmaster.kotlin.example.Person
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.CsvSource

@Tag("unitTest")
@DisplayName("ArgumentAccessor example")
class ArgumentsAccessorExampleTest {

    @ParameterizedTest
    @DisplayName("Should provide the correct arguments to the ArgumentsAccessor")
    @CsvSource("1, John Doe")
    fun shouldProvideCorrectParametersToArgumentsAccessor(arguments: ArgumentsAccessor) {
        val person = Person(arguments.getLong(0), arguments.getString(1))

        assertThat(person.id).isEqualByComparingTo(1L)
        assertThat(person.name).isEqualTo("John Doe")
    }
}