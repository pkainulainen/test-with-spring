package com.testwithspring.master.testcontainers;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.PreconditionViolationException;
import org.testcontainers.containers.GenericContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * This is a custom extension that starts a Docker container
 * before any test method is run and stops it after all test methods
 * have been run.
 *
 * If a test class uses this extension, it has must have a {@code static} field
 * that contains a reference to a configured {@code GenericContainer}, {@code PostgreSQLContainer},
 * or {@code BrowserWebDriverContainer} object.
 */
public class TestContainersExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        GenericContainer testContainer = getGenericContainerInstance(extensionContext.getRequiredTestClass());
        testContainer.stop();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        GenericContainer testContainer = getGenericContainerInstance(extensionContext.getRequiredTestClass());
        testContainer.start();
    }

    /**
     * Gets a reference to the started {@code GenericContainer} object.
     *
     * @param testClass The type of the test class.
     * @return  The found {@code GenericContainer} object.
     * @throws IllegalAccessException if the field cannot be accessed by using reflection.
     * @throws PreconditionViolationException if the test class doesn't have a {@code static GenericContainer} field.
     */
    private GenericContainer getGenericContainerInstance(Class<?> testClass) throws IllegalAccessException {
        Field testContainerField = Arrays.stream(testClass.getDeclaredFields())
                .filter(this::isContainerField)
                .findFirst()
                .orElseThrow(() -> new PreconditionViolationException("The test class doesn't have a GenericContainer field"));

        testContainerField.setAccessible(true);

        //We can pass null to this method because the field must be static
        return (GenericContainer) testContainerField.get(null);
    }

    private boolean isContainerField(Field f) {
        return Modifier.isStatic(f.getModifiers()) && GenericContainer.class.isAssignableFrom(f.getType());
    }
}
