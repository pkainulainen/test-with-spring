package com.testwithspring.master.kotlin.arguments

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.*

@Tag("unitTest")
@DisplayName("@NullSource and @EmptySource example")
class NullAndEmptySourceExampleTest {

    @DisplayName("Should pass a single null value as a function parameter")
    @ParameterizedTest(name = "{index} => text={0}")
    @NullSource
    fun shouldPassSingleNullValueAsFunctionParameter(text: String?) {
        assertThat(text).isNull()
    }

    @DisplayName("Should pass a single empty string as a function parameter")
    @ParameterizedTest(name = "{index} => text={0}")
    @EmptySource
    fun shouldPassSingleEmptyStringAsFunctionParameter(text: String) {
        assertThat(text).isEmpty()
    }

    @DisplayName("Should pass single null value and single empty string as function parameters")
    @ParameterizedTest(name = "{index} => text={0}")
    @NullAndEmptySource
    fun shouldPassSingleNullValueAndSingleEmptyStringAsFunctionParameters(text: String?) {
        assertThat(text).isNullOrEmpty()
    }

    @DisplayName("Should pass single null value, single empty string, and white space strings as function parameters")
    @ParameterizedTest(name = "{index} => text={0}")
    @NullAndEmptySource
    @ValueSource(strings = ["", " ", "\t", "\n"])
    fun shouldPassSingleNullValueSingleEmptyStringAndWhiteSpaceStringsAsFunctionParameters(text: String?) {
        assertThat(text?.trim()).isNullOrEmpty()
    }
}