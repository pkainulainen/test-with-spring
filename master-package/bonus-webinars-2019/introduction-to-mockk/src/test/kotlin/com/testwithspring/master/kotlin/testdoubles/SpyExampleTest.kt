package com.testwithspring.master.kotlin.testdoubles

import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Demonstrates how we can create a spy")
class SpyExampleTest {

    private lateinit var list: MutableList<Long>

    @BeforeEach
    fun createSpy() {
        list = spyk<ArrayList<Long>>()
        list.add(5)
    }

    @Test
    @DisplayName("Should return the real value")
    fun shouldReturnRealValue() {
        val returned = list.get(0)
        assertThat(returned).isEqualByComparingTo(5)
    }

    @Test
    @DisplayName("Should get the correct list item")
    fun shouldGetCorrectListItem() {
        list.get(0)
        verify { list.get(0) }
    }

    @Test
    @DisplayName("Should return the configured value")
    fun shouldReturnConfiguredValue() {
        every { list.get(1) } returns 99

        val returned = list.get(1)
        assertThat(returned).isEqualByComparingTo(99)
    }
}
