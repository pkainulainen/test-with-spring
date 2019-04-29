package com.testwithspring.master.kotlin.conversion

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@Tag("unitTest")
@DisplayName("Factory function conversion example")
class FactoryFunctionConversionExampleTest {

    @ParameterizedTest
    @DisplayName("Should create the function parameter with factory function")
    @ValueSource(strings = ["Moby Dick"])
    fun shouldCreateFunctionParameterWithFactoryFunction(book: Book) {
        assertThat(book.title).isEqualTo("Moby Dick")
    }
}