package com.testwithspring.master.kotlin.conversion

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@Tag("unitTest")
@DisplayName("Factory constructor conversion example")
class FactoryConstructorConversionExampleTest {

    @ParameterizedTest
    @DisplayName("Should create the function parameter with factory constructor")
    @ValueSource(strings = ["The Godfather"])
    fun shouldCreateFunctionParameterWithFactoryConstructor(movie: Movie) {
        assertThat(movie.title).isEqualTo("The Godfather")
    }
}