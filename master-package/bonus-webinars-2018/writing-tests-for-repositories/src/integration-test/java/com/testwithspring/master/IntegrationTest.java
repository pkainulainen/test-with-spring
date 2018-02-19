package com.testwithspring.master;

import com.testwithspring.master.config.Profiles;
import org.junit.jupiter.api.Tag;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Tag(TestTags.INTEGRATION_TEST)
public @interface IntegrationTest {
}
