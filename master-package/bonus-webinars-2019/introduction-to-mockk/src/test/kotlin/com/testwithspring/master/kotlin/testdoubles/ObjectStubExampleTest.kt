package com.testwithspring.master.kotlin.testdoubles

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

@Tag("unitTest")
@DisplayName("Demonstrates how we can stub Kotlin objects")
class ObjectStubExampleTest {

    @BeforeEach
    fun createStub() {
        mockkObject(Calculator)
    }

    @Test
    @DisplayName("Should return real value when no return value is configured")
    fun shouldReturnRealValueWhenNoReturnValueIsConfigured() {
        val sum = Calculator.add(1, 2)
        assertThat(sum).isEqualByComparingTo(3)
    }

    @Test
    @DisplayName("SHould return configured value")
    fun shouldReturnConfiguredValue() {
        every { Calculator.add(1, 2) } returns 99

        val sum = Calculator.add(1, 2)
        assertThat(sum).isEqualByComparingTo(99)
    }

    @AfterEach
    fun destroyStub() {
        unmockkObject(Calculator)
    }
}