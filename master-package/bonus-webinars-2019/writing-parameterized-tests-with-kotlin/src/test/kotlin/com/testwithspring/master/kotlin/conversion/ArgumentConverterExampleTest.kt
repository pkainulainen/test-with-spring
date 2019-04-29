package com.testwithspring.master.kotlin.conversion

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.converter.ConvertWith
import org.junit.jupiter.params.provider.ValueSource

@Tag("unitTest")
@DisplayName("Argument converter example")
class ArgumentConverterExampleTest {

    @ParameterizedTest
    @DisplayName("Should create the function parameter by using an argument converter")
    @ValueSource(strings = ["Yellow Submarine"])
    fun shouldCreateFunctionParameterWithArgumentConverter(@ConvertWith(AlbumArgumentConverter::class) album: Album) {
        assertThat(album.title).isEqualTo("Yellow Submarine")
    }
}