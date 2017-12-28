package com.testwithspring.master

import org.openqa.selenium.WebDriver
import org.spockframework.runtime.extension.IMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation

/**
 * This interceptor quits the {@code WebDriver} instance used by the
 * invoked specification and frees the reserved resources.
 */
class SeleniumWebDriverCleanUp implements IMethodInterceptor {

    @Override
    void intercept(IMethodInvocation invocation) throws Throwable {
        def webDriver = getWebDriver(invocation)
        webDriver?.quit()
        invocation.proceed()
    }

    private static WebDriver getWebDriver(IMethodInvocation invocation) {
        def webDriverField = invocation.getSpec()
                .getAllFields()
                .find({ it.isAnnotationPresent(SeleniumWebDriver.class)} )

        return webDriverField.readValue(invocation.getInstance())
    }
}
