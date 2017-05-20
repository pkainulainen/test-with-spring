package com.testwithspring.intermediate;


import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.List;

/**
 * This custom test runner reduces boilerplate code that is required
 * when we create and configure new {@code WebDriver} objects.
 *
 * When we use this test runner, we have to follow these rules:
 * <ul>
 *     <li>Our test class must be annotated with the {@code SeleniumTest} annotation.</li>
 *     <li>Our test class must have one field that is annotated with the {@code SeleniumWebDriver} annotation.</li>
 * </ul>
 *
 * The following code sample demonstrates the usage of this test runner:
 * <code>
 *     @RunWith(SeleniumTestRunner.class)
 *     @SeleniumTest(driver = ChromeDriver.class)
 *     public class ExampleSeleniumTest {
 *
 *     @SeleniumWebDriver
 *     private WebDriver browser;
 * }
 * </code>
 */
public class SeleniumTestRunner extends BlockJUnit4ClassRunner {

    private WebDriver webDriver;

    public SeleniumTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        super.collectInitializationErrors(errors);
        validateSeleniumTestAnnotation(errors);
        validateWebDriverField(errors);
    }

    private void validateSeleniumTestAnnotation(List<Throwable> errors) {
        if (!this.getTestClass().getJavaClass().isAnnotationPresent(SeleniumTest.class)) {
            String errorMessage = "Cannot instantiate WebDriver because the test class " +
                    "is not annotated with the @SeleniumTest annotation";

            errors.add(new RuntimeException(errorMessage));
        }
    }

    private void validateWebDriverField(List<Throwable> errors) {
        List<FrameworkField> fields = this.getTestClass().getAnnotatedFields(SeleniumWebDriver.class);

        if (fields.isEmpty()) {
            String errorMessage = "Cannot initialize test class because it has no field " +
                    "that is annotated with the @SeleniumWebDriver annotation";
            errors.add(new RuntimeException(errorMessage));
        }

        if (fields.size() > 1) {
            String errorMessage = "Cannot initialize test class because it has multiple fields " +
                    "that are annotated with the @SeleniumWebDriver annotation";
            errors.add(new RuntimeException(errorMessage));
        }

        if (fields.size() == 1) {
            FrameworkField field = fields.get(0);
            if (!field.getType().equals(WebDriver.class)) {
                String errorMessage = "Cannot initialize test class because the type of the field " +
                        "that is annotated with the @SeleniumWebDriver annotation is not WebDriver";
                errors.add(new RuntimeException(errorMessage));
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        this.webDriver = createBrowserBeforeTests();

        super.run(notifier);

        closeBrowserAfterTests();
    }

    private WebDriver createBrowserBeforeTests() {
        SeleniumTest seleniumConfig = this.getTestClass().getAnnotation(SeleniumTest.class);
        return createWebDriver(seleniumConfig.driver());
    }

    private WebDriver createWebDriver(Class<? extends WebDriver> webDriverClass) {
        try {
            return webDriverClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format(
                    "Cannot instantiate WebDriver. Is %s a non abstract class that has no argument constructor?",
                    webDriverClass.getCanonicalName()
            ));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format(
                    "Cannot instantiate WebDriver. Does %s have a public constructor?",
                    webDriverClass.getCanonicalName()
            ));
        }
    }

    private void closeBrowserAfterTests() {
        if (this.webDriver != null) {
            this.webDriver.quit();
        }
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        injectWebDriverIntoTestObject(target);
        return super.withBefores(method, target, statement);
    }

    private void injectWebDriverIntoTestObject(Object testObject) {
        Field webDriverField = getWebDriverField(testObject);
        webDriverField.setAccessible(true);
        try {
            webDriverField.set(testObject, this.webDriver);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not inject WebDriver object because an error occurred", e);
        }
    }

    private Field getWebDriverField(Object testObject) {
        Field[] fields = testObject.getClass().getDeclaredFields();

        for (int index = 0; index < fields.length; index++) {
            Field field = fields[index];
            if (field.isAnnotationPresent(SeleniumWebDriver.class) && field.getType().equals(WebDriver.class)) {
                return field;
            }
        }

        throw new RuntimeException("No WebDriver field was found.");
    }
}
