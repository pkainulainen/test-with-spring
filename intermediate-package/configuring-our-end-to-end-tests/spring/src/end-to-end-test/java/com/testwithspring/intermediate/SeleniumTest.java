package com.testwithspring.intermediate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.lang.annotation.*;

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
public @interface SeleniumTest {

    /**
     *  Configures the type of the {@WebDriver} object that runs our end-to-end tests.
     */
    Class<? extends WebDriver> driver() default ChromeDriver.class;
}
