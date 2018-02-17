package com.testwithspring.master

import org.openqa.selenium.WebDriver
import org.spockframework.runtime.InvalidSpecException
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation
import org.spockframework.runtime.model.FieldInfo
import org.spockframework.runtime.model.SpecInfo
import spock.lang.Shared

/**
 * This interceptor has the following responsibilities:
 * <ul>
 *     <li>
 *         Ensure that the specification class has exactly one shared {@code WebDriver} field
 *         that is annotated with the {@link SeleniumWebDriver} annotation.
 *     </li>
 *     <li>
 *         Create a new {@code WebDriver} object by using the configuration provided
 *         the {@link SeleniumTest} annotation.
 *     </li>
 *     <li>
 *         Inject the created {@code WebDriver} object into the invoked specification.
 *     </li>
 * </ul>
 */
class SeleniumWebDriverInitializer implements IMethodInterceptor {

    private final Class<? extends WebDriver> webDriverClass

    protected SeleniumWebDriverInitializer(Class<? extends WebDriver> webDriverClass) {
        this.webDriverClass = webDriverClass
    }

    @Override
    void intercept(IMethodInvocation invocation) throws Throwable {
        def webDriverField = getWebDriverField(invocation.getSpec())
        def webDriver = createWebDriver()
        webDriverField.writeValue(invocation.getInstance(), webDriver)
        invocation.proceed()
    }

    private static FieldInfo getWebDriverField(SpecInfo spec) {
        def fields = spec.getAllFields()
        def webDriverFields = fields.findAll({ it.isAnnotationPresent(SeleniumWebDriver.class) })

        if (webDriverFields.isEmpty()) {
            throw new InvalidSpecException(
                    'Cannot initialize specification class because ' +
                            'it has no field that is annotated with the ' +
                            '@SeleniumWebDriver annotation'
            )
        }

        if (webDriverFields.size() > 1) {
            throw new InvalidSpecException(
                    'Cannot initialize specification class because ' +
                            'it has multiple fields that are annotated with ' +
                            'the @SeleniumWebDriver annotation'
            )
        }

        def webDriverField = webDriverFields[0]
        if (!webDriverField.isAnnotationPresent(Shared.class)) {
            throw new InvalidSpecException(
                    'Cannot initialize specification class because ' +
                            'the WebDriver field is not shared'
            )
        }

        if (!webDriverField.getType().equals(WebDriver.class)) {
            throw new InvalidSpecException(
                    'Cannot initialize specification class because ' +
                            'the type of the field that is annotated with the ' +
                            '@SeleniumWebDriver annotation is not WebDriver'
            )
        }

        return webDriverField
    }

    private WebDriver createWebDriver() {
        try {
            return webDriverClass.newInstance()
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format(
                    'Cannot instantiate WebDriver. Is %s a non abstract ' +
                            'class that has no argument constructor?',
                    webDriverClass.getCanonicalName()
            ))
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format(
                    'Cannot instantiate WebDriver. Does %s have a ' +
                            'public constructor?',
                    webDriverClass.getCanonicalName()
            ))
        }
    }
}
