package com.testwithspring.intermediate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("@EnumSource example")
class EnumSourceExampleTest {

    @DisplayName("Should pass all enum values as method parameters")
    @ParameterizedTest(name = "{index} => pet=''{0}''")
    @EnumSource(Pet.class)
    void shouldPassNotNullEnumValuesAsMethodParameter(Pet pet) {
        assertNotNull(pet);
    }

    @DisplayName("Should pass only the specified enum value as a method parameter")
    @ParameterizedTest(name = "{index} => pet=''{0}''")
    @EnumSource(value = Pet.class, names = {"CAT"})
    void shouldPassNotNullEnumValueAsMethodParameter(Pet pet) {
        assertNotNull(pet);
    }
}
