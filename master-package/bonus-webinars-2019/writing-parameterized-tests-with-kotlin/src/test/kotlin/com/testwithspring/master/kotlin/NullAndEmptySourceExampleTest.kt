package com.testwithspring.master.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.*

@Tag("unitTest")
@DisplayName("@NullSource and @EmptySource example")
class NullAndEmptySourceExampleTest {

    @DisplayName("Should pass single null value as a method parameter")
    @ParameterizedTest(name = "{index} => text={0}")
    @NullSource
    fun shouldPassSingleNullValueAsMethodParameter(text: String?) {
        assertThat(text).isNull()
    }

    @DisplayName("Should pass single empty string as a method parameter")
    @ParameterizedTest(name = "{index} => text={0}")
    @EmptySource
    fun shouldPassSingleEmptyStringAsMethodParameter(text: String) {
        assertThat(text).isEmpty()
    }

    @DisplayName("Should pass single null value and single empty string as method parameters")
    @ParameterizedTest(name = "{index} => text={0}")
    @NullAndEmptySource
    fun shouldPassSingleNullValueAndSingleEmptyStringAsMethodParameters(text: String?) {
        assertThat(text).isNullOrEmpty()
    }

    @DisplayName("Should pass single null value, single empty string, and white space strings as method parameters")
    @ParameterizedTest(name = "{index} => text={0}")
    @NullAndEmptySource
    @ValueSource(strings = ["", " ", "\t", "\n"])
    fun shouldPassSingleNullValueSingleEmptyStringAndWhiteSpaceStringsAsMethodParameters(text: String?) {
        assertThat(text?.trim()).isNullOrEmpty()
    }
}