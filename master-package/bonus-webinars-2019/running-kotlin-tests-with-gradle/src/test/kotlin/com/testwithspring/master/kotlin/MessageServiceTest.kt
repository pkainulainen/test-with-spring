package com.testwithspring.master.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unitTest")
class MessageServiceTest {

    private lateinit var service: MessageService

    @BeforeEach
    fun configureSystemUnderTest() {
        service = MessageService()
    }

    @Test
    @DisplayName("Should return the correct message")
    fun shouldReturnCorrectMessage() {
        val message = service.getMessage()
        assertThat(message).isEqualTo("Hello World!")
    }
}