package com.testwithspring.master.kotlin.testdoubles

import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@Tag("unitTest")
@DisplayName("Demonstrates how we can mock Kotlin objects")
class ObjectMockExampleTest {

    @BeforeEach
    fun createMock() {
        mockkObject(Calculator)
    }

    @Test
    @DisplayName("Should pass the correct numbers to calculator")
    fun shouldPassCorrectNumbersToCalculator() {
        Calculator.add(1, 2)

        verify { Calculator.add(1, 2) }
    }

    @AfterEach
    fun destroyMock() {
        unmockkObject(Calculator)
    }
}