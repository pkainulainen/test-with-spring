package com.testwithspring.master.dbunitdatatypes.config;

import java.lang.annotation.*;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.testwithspring.master.dbunitdatatypes.CustomDbUnitDataTypeApplication;
import com.testwithspring.master.dbunitdatatypes.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

/**
 * This annotation helps us to reduce duplicate configuration when we are
 * writing integration tests for code that interacts with a relational database.
 * When we want to write integration tests for a class that interacts with a relational
 * database, we have to annotate our integration test class with this annotation.
 *
 * Also, if we have to configure the used DbUnit data sets by annotating our
 * test class with the [com.github.springtestdbunit.annotation.DatabaseSetup] annotation.
 * If we want to provide extra configuration for DbUnit, we have to annotate our
 * test class with the [com.github.springtestdbunit.annotation.DbUnitConfiguration] annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest(classes = {CustomDbUnitDataTypeApplication.class, TestDatabaseConfiguration.class})
@TestExecutionListeners(
        listeners = {DbUnitTestExecutionListener.class},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@IntegrationTest
public @interface DatabaseIntegrationTest {
}
