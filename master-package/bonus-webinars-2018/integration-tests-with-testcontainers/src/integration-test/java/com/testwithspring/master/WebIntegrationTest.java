package com.testwithspring.master;

import com.testwithspring.master.config.Profiles;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;

/**
 * This annotation helps us to reduce duplicate configuration when we are
 * writing integration tests for Spring MVC controllers. When we want to write integration
 * tests for a Sprng MVC controller, we have to annotate our integration class with this annotation.
 * Also, if we have to configure the used DbUnit data sets by annotating our
 * test class with the {@link com.github.springtestdbunit.annotation.DatabaseSetup} annotation.
 * If we want to provide extra configuration for DbUnit, we have to annotate our
 * test class with the {@link com.github.springtestdbunit.annotation.DbUnitConfiguration} annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(value =  {
        OrderedDbUnitTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
},
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Tag(TestTags.INTEGRATION_TEST)
public @interface WebIntegrationTest {

    @AliasFor(
            annotation = SpringBootTest.class
    )
    Class<?>[] classes() default {};
}
