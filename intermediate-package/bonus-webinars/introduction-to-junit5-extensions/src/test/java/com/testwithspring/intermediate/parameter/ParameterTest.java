package com.testwithspring.intermediate.parameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MessageResolverExtension.class)
class ParameterTest {

    @Test
    void shouldPassCorrectMessageAsMethodParameter(String message) {
        assertEquals("Hello World!", message);
    }
}
