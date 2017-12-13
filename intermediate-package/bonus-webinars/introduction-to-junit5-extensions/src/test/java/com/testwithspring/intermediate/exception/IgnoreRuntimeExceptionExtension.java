package com.testwithspring.intermediate.exception;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

/**
 * This extension ignores runtime extensions
 */
public class IgnoreRuntimeExceptionExtension implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (RuntimeException.class.isAssignableFrom(throwable.getClass())) {
            System.out.println(String.format("Ignoring runtime exception: %s",
                    throwable.getClass()
            ));
            return;
        }

        throw throwable;
    }
}
