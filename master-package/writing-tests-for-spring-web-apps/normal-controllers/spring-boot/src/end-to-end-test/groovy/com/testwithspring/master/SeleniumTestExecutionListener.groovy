package com.testwithspring.master

import org.openqa.selenium.WebDriver
import org.springframework.beans.BeanUtils
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener

/**
 * Creates a new {@code WebDriver} bean by using the configuration provided by
 * the {@code @SeleniumTest} annotation. This {@code TestExecutionListener} will
 * also destroy the created bean after all feature methods of the specification class
 * have been run.
 */
class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

    def webDriver

    /**
     * We need to ensure that this listener is the first test execution listener
     * because otherwise the {@code WebDriver} bean is not found when it is
     * injected into the test class.
     * @return
     */
    @Override
    int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE
    }

    @Override
    void prepareTestInstance(TestContext testContext) throws Exception {
        if (webDriver != null) {
            return
        }
        def context = testContext.getApplicationContext()
        if (context instanceof ConfigurableApplicationContext) {
            webDriver = createConfiguredWebDriver(testContext)
            registerWebDriverBean(context, webDriver)
        }
    }

    private createConfiguredWebDriver(TestContext testContext) {
        SeleniumTest seleniumConfig = AnnotationUtils.findAnnotation(testContext.getTestClass(), SeleniumTest.class)
        return BeanUtils.instantiate(seleniumConfig.driver())
    }

    private registerWebDriverBean(ApplicationContext targetContext, WebDriver bean) {
        def configurableApplicationContext = (ConfigurableApplicationContext) targetContext
        def bf = configurableApplicationContext.getBeanFactory()
        bf.registerResolvableDependency(WebDriver.class, bean)
    }

    @Override
    void afterTestClass(TestContext testContext) throws Exception {
        if (webDriver != null) {
            webDriver.quit()
        }
    }
}
