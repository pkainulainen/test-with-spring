package com.testwithspring.master.kotlin.aggregation

import com.testspringmaster.kotlin.example.Person
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.provider.CsvSource

@Tag("unitTest")
@DisplayName("PersonAggregator example")
class PersonAggregatorExampleTest {

    @ParameterizedTest
    @DisplayName("Should pass a Person object as function parameter")
    @CsvSource("1, John Doe")
    fun shouldPassPersonObjectAsFunctionParameter(@AggregateWith(PersonAggregator::class) person: Person) {
        assertThat(person.id).isEqualByComparingTo(1L)
        assertThat(person.name).isEqualTo("John Doe")
    }
}