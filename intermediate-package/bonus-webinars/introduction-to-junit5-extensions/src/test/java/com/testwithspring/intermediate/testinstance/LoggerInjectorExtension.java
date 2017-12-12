package com.testwithspring.intermediate.testinstance;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * This extension injects a new {@link Logger} object into the test class.
 * If the test class doesn't have a {@link Logger} field, this extension
 * throws a {@link RuntimeException}.
 */
public class LoggerInjectorExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception {
        Logger logger = LoggerFactory.getLogger(o.getClass());
        Field loggerField = findLoggerField(o);
        loggerField.setAccessible(true);
        loggerField.set(o, logger);
    }

    private Field findLoggerField(Object source) {
        return Arrays.asList(source.getClass().getDeclaredFields())
                .stream()
                .filter(f -> f.getType().isAssignableFrom(Logger.class))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No Logger field found from the test class"));
    }
}
