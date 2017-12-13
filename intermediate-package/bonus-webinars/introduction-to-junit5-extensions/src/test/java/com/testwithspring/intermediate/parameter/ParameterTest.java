package com.testwithspring.intermediate.parameter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MessageResolverExtension.class)
@DisplayName("Resolve the values of String parameters")
class ParameterTest {

    @Test
    @DisplayName("Should pass correct message as a method parameter")
    void shouldPassCorrectMessageAsMethodParameter(String message) {
        assertEquals("Hello World!", message);
    }
}
