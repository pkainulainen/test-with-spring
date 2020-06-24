package com.testwithspring.master

import com.github.springtestdbunit.DbUnitTestExecutionListener
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * This annotation helps us to reduce duplicate configuration when we are
 * writing integration tests for code that interacts with a relational database.
 * When we want to write integration tests for a class that interacts with a relational
 * database, we have to annotate our integration test class with this annotation.
 * Also, if we have to configure the used DbUnit data sets by annotating our
 * test class with the [com.github.springtestdbunit.annotation.DatabaseSetup] annotation.
 * If we want to provide extra configuration for DbUnit, we have to annotate our
 * test class with the [com.github.springtestdbunit.annotation.DbUnitConfiguration] annotation.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest
@TestExecutionListeners(
        listeners = [OrderedDbUnitTestExecutionListener::class],
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@ActiveProfiles("integrationTest")
@Tag("integrationTest")
annotation class DatabaseIntegrationTest