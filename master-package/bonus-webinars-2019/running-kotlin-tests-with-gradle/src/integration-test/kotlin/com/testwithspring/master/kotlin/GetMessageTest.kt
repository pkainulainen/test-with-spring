package com.testwithspring.master.kotlin

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("integrationTest")
class GetMessageTest {

    private lateinit var service: MessageService

    @BeforeEach
    fun configureSystemUnderTest() {
        service = MessageService()
    }

    @Test
    @DisplayName("Should return the correct message")
    fun shouldReturnCorrectMessage() {
        val message = service.getMessage()
        Assertions.assertThat(message).isEqualTo("Hello World!")
    }
}