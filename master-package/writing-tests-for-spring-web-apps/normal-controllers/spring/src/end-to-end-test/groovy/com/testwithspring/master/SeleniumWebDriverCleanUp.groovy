package com.testwithspring.master

import org.openqa.selenium.WebDriver
import org.spockframework.runtime.extension.AbstractMethodInterceptor
import org.spockframework.runtime.extension.IMethodInvocation

/**
 * This interceptor quits the {@code WebDriver} instance used by the
 * invoked specification and frees the reserved resources.
 */
class SeleniumWebDriverCleanUp extends AbstractMethodInterceptor {

    @Override
    void interceptCleanupSpecMethod(IMethodInvocation invocation) throws Throwable {
        def webDriver = getWebDriver(invocation)

        if (webDriver != null) {
            webDriver.quit()
        }
    }

    private static WebDriver getWebDriver(IMethodInvocation invocation) {
        def webDriverField = invocation.getSpec()
                .getAllFields()
                .find({ it.isAnnotationPresent(SeleniumWebDriver.class)} )

        return webDriverField.readValue(invocation.getInstance())
    }
}
