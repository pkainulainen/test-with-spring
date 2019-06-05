package com.testwithspring.master.kotlin.testdoubles

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Demonstrates how we can create a relaxed stub")
class RelaxedStubExampleTest {

    //Relaxed stubbing isn't working properly with a generic
    //return type. Thus, we will use * here. Also, we can use
    //a workaround that's documented here: https://github.com/mockk/mockk#relaxed-mock
    private lateinit var list: List<*>

    @BeforeEach
    fun createStub() {
        list = mockk<List<*>>(relaxed = true)
    }

    @Test
    @DisplayName("Should return the configured value")
    fun shouldReturnConfiguredValue() {
        every { list.get(0) } returns 5

        val returned = list.get(0)
        assertThat(returned).isEqualTo(5)
    }

    @Test
    @DisplayName("Should return the default value when the return value isn't configured")
    fun shouldReturnDefaultValueWhenNoValueIsConfigured() {
        val returned = list.get(1)
        assertThat(returned).isNotNull()
    }
}
