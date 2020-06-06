package com.testwithspring.master.web

import com.testwithspring.master.DatabaseIntegrationTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

/**
 * This annotation helps us to reduce duplicate configuration code when
 * we write integration tests for Spring MVC controllers. Thus, when we
 * want to write an integration test for a Spring MVC controller, we have
 * to annotate our test class with this annotation.
 *
 * Also, if we have to configure the used DbUnit data sets by annotating our
 * test class with the [com.github.springtestdbunit.annotation.DatabaseSetup] annotation.
 * If we want to provide extra configuration for DbUnit, we have to annotate our
 * test class with the [com.github.springtestdbunit.annotation.DbUnitConfiguration] annotation.
 *
 * @see DatabaseIntegrationTest
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@AutoConfigureMockMvc
@DatabaseIntegrationTest
annotation class WebIntegrationTest {
}