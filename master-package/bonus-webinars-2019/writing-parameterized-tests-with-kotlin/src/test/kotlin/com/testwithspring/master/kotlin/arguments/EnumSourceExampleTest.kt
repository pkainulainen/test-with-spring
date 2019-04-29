package com.testwithspring.master.kotlin.arguments

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@Tag("unitTest")
@DisplayName("@EnumSource example")
class EnumSourceExampleTest {

    @DisplayName("Should pass all enum values as function parameters")
    @ParameterizedTest(name = "{index} => pet=''{0}''")
    @EnumSource(Pet::class)
    fun shouldPassNotNullEnumValuesAsFunctionParameter(pet: Pet) {
        assertThat(pet).isNotNull()
    }

    @DisplayName("Should pass only the specified enum value as a function parameter")
    @ParameterizedTest(name = "{index} => pet=''{0}''")
    @EnumSource(value = Pet::class, names = ["CAT"])
    fun shouldPassNotNullEnumValueAsFunctionParameter(pet: Pet) {
        assertThat(pet).isEqualTo(Pet.CAT)
    }
}