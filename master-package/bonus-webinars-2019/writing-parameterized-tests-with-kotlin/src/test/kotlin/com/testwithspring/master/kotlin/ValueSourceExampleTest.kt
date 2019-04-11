package com.testwithspring.master.kotlin

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag

@Tag("unitTest")
@DisplayName("@ValueSource example")
class ValueSourceExampleTest {

    @DisplayName("Should pass the function parameters provided by the @ValueSource annotation")
    @ParameterizedTest(name = "{index} => message=''{0}''")
    @ValueSource(strings = ["Hello", "World"])
    fun shouldPassNotNullMessageAsFunctionParameter(message: String) {
        assertNotNull(message)
    }
}