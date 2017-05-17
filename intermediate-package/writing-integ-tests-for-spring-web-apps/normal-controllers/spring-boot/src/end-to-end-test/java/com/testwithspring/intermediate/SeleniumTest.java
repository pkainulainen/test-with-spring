package com.testwithspring.intermediate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

/**
 * This annotation should be added to a class that runs Selenium tests. This annotation provides
 * the following features:
 * <ul>
 *     <li>
 *         Registers a {@SeleniumTestExecutionListener} class which can setup and clean up
 *         {@code WebDriver} objects.
 *     </li>
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
@TestExecutionListeners(listeners = {SeleniumTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
public @interface SeleniumTest {

    Class<? extends WebDriver> driver() default ChromeDriver.class;
}
