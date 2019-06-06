package com.testwithspring.master.kotlin.annotations

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Demonstrates how we can create test doubles with annotations")
class AnnotationTest {

    @MockK
    private lateinit var mockList: List<Long>

    @MockK(relaxed = true)
    private lateinit var relaxedMockList: List<Long>

    @SpyK
    private var spyList = ArrayList<Long>()

    @MockK
    private lateinit var stubList: List<Long>

    @RelaxedMockK
    private lateinit var relaxedStubList: List<*>

    @BeforeEach
    fun createTestDoubles() {
        MockKAnnotations.init(this)
    }

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
        assertThat(returned).isEqualByComparingTo(5)
    }

    @Test
    @DisplayName("Should return the configured value")
    fun shouldReturnConfiguredValue() {
        every { stubList.get(0) } returns 5

        val returned = stubList.get(0)
        assertThat(returned).isEqualByComparingTo(5)
    }

    @Test
    @DisplayName("Should return the default value when the return value isn't configured")
    fun shouldReturnDefaultValueWhenNoValueIsConfigured() {
        val returned = relaxedStubList.get(1)
        assertThat(returned).isNotNull()
    }
}