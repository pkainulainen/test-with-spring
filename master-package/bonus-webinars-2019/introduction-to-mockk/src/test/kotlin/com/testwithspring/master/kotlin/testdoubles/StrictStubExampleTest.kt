package com.testwithspring.master.kotlin.testdoubles

import io.mockk.MockKException
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Demonstrates how we can create a strict stub")
class StrictStubExampleTest {

    private lateinit var list: List<Long>

    @BeforeEach
    fun createStub() {
        list = mockk<List<Long>>()
    }

    @Test
    @DisplayName("Should return the configured value")
    fun shouldReturnConfiguredValue() {
        every { list.get(0) } returns 5

        val returned = list.get(0)
        assertThat(returned).isEqualByComparingTo(5)
    }

    @Test
    @DisplayName("Should throw an exception when the return value isn't configured")
    fun shouldThrowExceptionWhenNoValueIsConfigured() {
        assertThatThrownBy { list.get(1) }
                .isExactlyInstanceOf(MockKException::class.java)
    }
}
