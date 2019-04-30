package com.testwithspring.master.kotlin.example

import com.testspringmaster.kotlin.example.NotFoundException
import com.testspringmaster.kotlin.example.Person
import com.testspringmaster.kotlin.example.PersonRepository
import com.testwithspring.master.kotlin.aggregation.PersonAggregator
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.provider.CsvSource

@Tag("unitTest")
@DisplayName("A parameterized test for a finder method")
class PersonRepositoryTest{

    private val repository = PersonRepository()

    @Nested
    @DisplayName("Find person by id")
    inner class FindById {

        @Nested
        @DisplayName("When the person is not found")
        inner class WhenPersonIsNotFound {

            private val ID_NOT_FOUND = 999L

            @Test
            @DisplayName("Should throw an exception")
            fun shouldThrowException() {
                val thrown = catchThrowable { repository.findById(ID_NOT_FOUND) }
                assertThat(thrown).isExactlyInstanceOf(NotFoundException::class.java)
            }
        }

        @Nested
        @DisplayName("When the person is found")
        inner class WhenPersonIsFound {

            @ParameterizedTest
            @DisplayName("Should return the found person")
            @CsvSource(
                    "1, John Doe",
                    "2, Jane Doe"
            )
            fun shouldReturnFoundPerson(@AggregateWith(PersonAggregator::class) person: Person) {
                val found = repository.findById(person.id)

                assertThat(found.id).isEqualByComparingTo(person.id)
                assertThat(found.name).isEqualTo(person.name)
            }
        }
    }
}