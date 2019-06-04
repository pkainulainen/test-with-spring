package com.testwithspring.master.kotlin.junit5

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@DisplayName("Demonstrates how we can integrate mockk with junit 5")
class JUnit5Test {

    @MockK
    private lateinit var mockList: List<Long>

    @MockK(relaxed = true)
    private lateinit var relaxedMockList: List<Long>

    @SpyK
    private var spyList = ArrayList<Long>()

    @MockK
    private lateinit var stubList: List<Long>

    @MockK(relaxed = true)
    private lateinit var relaxedStubList: List<*>

    @Test
    @DisplayName("Should get the correct list item from strict mock")
    fun shouldGetCorrectListItemFromStrictMock() {
        every { mockList.get(0) } returns 5

        mockList.get(0)
        verify { mockList.get(0) }
    }

    @Test
    @DisplayName("Should get the correct list item from relaxed mock")
    fun shouldGetCorrectListItemFromRelaxedMock() {
        relaxedMockList.get(0)
        verify { relaxedMockList.get(0) }
    }

    @Test
    @DisplayName("Should return the real value")
    fun shouldReturnRealValue() {
        spyList.add(5)

        val returned = spyList.get(0)
        Assertions.assertThat(returned).isEqualByComparingTo(5)
    }

    @Test
    @DisplayName("Should return the configured value")
    fun shouldReturnConfiguredValue() {
        every { stubList.get(0) } returns 5

        val returned = stubList.get(0)
        Assertions.assertThat(returned).isEqualByComparingTo(5)
    }

    @Test
    @DisplayName("Should return the default value when the return value isn't configured")
    fun shouldReturnDefaultValueWhenNoValueIsConfigured() {
        val returned = relaxedStubList.get(1)
        Assertions.assertThat(returned).isNotNull()
    }
}