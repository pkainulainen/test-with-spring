package com.testwithspring.master.kotlin.testdoubles

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
@DisplayName("Demonstrates how we can create a relaxed mock")
class RelaxedMockExampleTest {

    private lateinit var list: List<Long>

    @BeforeEach
    fun createMock() {
        list = mockk<List<Long>>(relaxed = true)
    }

    @Test
    @DisplayName("Should get the correct list item")
    fun shouldReturnConfiguredValue() {
        list.get(0)
        verify { list.get(0) }
    }
}
