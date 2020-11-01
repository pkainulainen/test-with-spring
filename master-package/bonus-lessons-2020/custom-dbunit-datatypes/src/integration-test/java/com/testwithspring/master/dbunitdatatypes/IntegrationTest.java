package com.testwithspring.master.dbunitdatatypes;

import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

/**
 * This annotation marks a test class as an integration test class.
 * If you annotate a test class with this annotation, the
 * methods of the annotated class are run when the integration tests
 * are run by the Maven Failsafe plugin.
 *
 * Also, this class configures the active Spring profile which
 * allows us to use test specific configuration.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ActiveProfiles("integrationTest")
@Tag("integrationTest")
public @interface IntegrationTest {
}
