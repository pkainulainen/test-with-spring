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
