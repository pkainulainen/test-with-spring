package com.testwithspring.intermediate.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(IgnoreRuntimeExceptionExtension.class)
public class ExceptionTest {

    @Test
    void shouldPassWhenRuntimeExceptionIsThrown() {
        throw new RuntimeException();
    }

    @Test
    void shouldPassWhenSubClassOfRunTimeExceptionIsThrown() {
        throw new IllegalStateException();
    }

    @Test
    void shouldFailWhenExceptionIsThrown() throws Exception {
        throw new Exception();
    }
}
