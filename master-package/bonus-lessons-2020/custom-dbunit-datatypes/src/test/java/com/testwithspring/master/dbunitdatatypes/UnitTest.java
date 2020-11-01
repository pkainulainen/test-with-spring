package com.testwithspring.master.dbunitdatatypes;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

/**
 * This annotation marks a test class as a unit test class.
 * If you annotate a test class with this annotation, the
 * methods of the annotated class are run when the unit tests
 * are run by the Maven Surefire plugin.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Tag("unitTest")
public @interface UnitTest {
}
