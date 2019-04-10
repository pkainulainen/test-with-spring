package com.testwithspring.master.kotlin

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag

@Tag("unitTest")
@DisplayName("@EnumSource example")
class EnumSourceExampleTest {

    @DisplayName("Should pass all enum values as method parameters")
    @ParameterizedTest(name = "{index} => pet=''{0}''")
    @EnumSource(Pet::class)
    fun shouldPassNotNullEnumValuesAsMethodParameter(pet: Pet) {
        assertNotNull(pet)
    }

    @DisplayName("Should pass only the specified enum value as a method parameter")
    @ParameterizedTest(name = "{index} => pet=''{0}''")
    @EnumSource(value = Pet::class, names = ["CAT"])
    fun shouldPassNotNullEnumValueAsMethodParameter(pet: Pet) {
        assertNotNull(pet)
    }
}