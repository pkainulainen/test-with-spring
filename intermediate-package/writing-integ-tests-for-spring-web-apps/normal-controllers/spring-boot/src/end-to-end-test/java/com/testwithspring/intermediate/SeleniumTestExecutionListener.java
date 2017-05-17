package com.testwithspring.intermediate;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Creates a new {@code WebDriver} bean by using the configuration provided by
 * the {@code @SeleniumTest} annotation. This {@code TestExecutionListener} will
 * also destroy the created bean after all test methods of the test class
 * have been run.
 */
public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

    private WebDriver webDriver;

    /**
     * We need to ensure that this listener is the first test execution listener
     * because otherwise the {@code WebDriver} bean is not found when it is
     * injected into the test class.
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        if (webDriver != null) {
            return;
        }
        ApplicationContext context = testContext.getApplicationContext();
        if (context instanceof ConfigurableApplicationContext) {
            webDriver = createConfiguredWebDriver(testContext);
            registerWebDriverBean(context, webDriver);
        }
    }

    private WebDriver createConfiguredWebDriver(TestContext testContext) {
        SeleniumTest seleniumConfig = AnnotationUtils.findAnnotation(
                testContext.getTestClass(), SeleniumTest.class);
        return BeanUtils.instantiate(seleniumConfig.driver());
    }

    private void registerWebDriverBean(ApplicationContext targetContext, WebDriver bean) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) targetContext;
        ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
        bf.registerResolvableDependency(WebDriver.class, bean);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}

