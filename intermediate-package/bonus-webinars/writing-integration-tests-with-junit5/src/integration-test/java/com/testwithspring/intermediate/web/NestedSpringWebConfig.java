package com.testwithspring.intermediate.web;

import org.junit.jupiter.api.Nested;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.*;

/**
 * This annotation allows use to create a nested test class
 * and configure the application context configuration classes
 * which configure the application context that is loaded before
 * the test methods of the nested test class are invoked.
 */
@Nested
@ContextConfiguration
@WebAppConfiguration
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface NestedSpringWebConfig {

    @AliasFor(
            annotation = ContextConfiguration.class
    )
    Class<?>[] classes() default {};
}
