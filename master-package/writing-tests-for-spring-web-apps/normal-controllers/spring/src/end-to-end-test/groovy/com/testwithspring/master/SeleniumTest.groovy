package com.testwithspring.master

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target


/**
 * This annotation should be added to a class that runs Selenium tests. This annotation provides
 * the following features:
 * <ul>
 *     <li>
 *         Provides a configuration option that is used to configure the type of the
 *         created {@code WebDriver object}.
 *     </li>
 * </ul>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtensionAnnotation(SeleniumWebDriverExtension.class)
@interface SeleniumTest {

    /**
     *  Configures the type of the {@WebDriver} object that runs our end-to-end tests.
     */
    Class<? extends WebDriver> driver() default ChromeDriver.class
}