package com.testwithspring.intermediate;

import java.lang.annotation.*;

/**
 * This annotation is a marker annotation that is used to
 * identify the field that a contains a reference to used
 * {@code WebDriver} object.
 *
 * When we use this annotation, we have to follow these
 * rules:
 * <ul>
 *     <li>One test class can have only one field that is annotated with this annotation.</li>
 *     <li>The type of the annotated field must be: {@code WebDriver}.</li>
 * </ul>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SeleniumWebDriver {

}
