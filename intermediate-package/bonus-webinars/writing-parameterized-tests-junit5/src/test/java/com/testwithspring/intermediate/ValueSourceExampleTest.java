package com.testwithspring.intermediate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("@ValueSource example")
class ValueSourceExampleTest {

    @DisplayName("Should pass the method parameters provided by the @ValueSource annotation")
    @ParameterizedTest(name = "{index} => message=''{0}''")
    @ValueSource(strings = {"Hello", "World"})
    void shouldPassNotNullMessageAsMethodParameter(String message) {
        assertNotNull(message);
    }
}
