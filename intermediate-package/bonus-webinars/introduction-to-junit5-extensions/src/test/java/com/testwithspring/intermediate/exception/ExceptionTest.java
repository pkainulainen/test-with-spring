package com.testwithspring.intermediate.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IgnoreRuntimeExceptionExtension.class)
@DisplayName("Handle exceptions")
public class ExceptionTest {

    @Test
    @DisplayName("Should ignore the RuntimeException")
    void shouldPassWhenRuntimeExceptionIsThrown() {
        throw new RuntimeException();
    }

    @Test
    @DisplayName("Should ignore the IllegalStateException")
    void shouldPassWhenSubClassOfRunTimeExceptionIsThrown() {
        throw new IllegalStateException();
    }

    @Test
    @DisplayName("Should fail the test")
    void shouldFailWhenExceptionIsThrown() throws Exception {
        throw new Exception();
    }
}
