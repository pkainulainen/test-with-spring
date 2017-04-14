package com.testwithspring.intermediate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

@Configuration
@Import({
        PersistenceContext.class,
        WebMvcContext.class
})
public class ExampleApplicationContext {

    /**
     * These static classes are required because it makes it possible to use
     * different properties files for every Spring profile.
     *
     * See: <a href="http://stackoverflow.com/a/14167357/313554">This StackOverflow answer</a>
     * for more details.
     */
    @Profile(Profiles.APPLICATION)
    @Configuration
    @PropertySource("classpath:application.properties")
    static class ApplicationProperties {}

    @Profile(Profiles.END_TO_END_TEST)
    @Configuration
    @PropertySource("classpath:end-to-end-test.properties")
    static class EndToEndTestProperties {}

    @Profile(Profiles.INTEGRATION_TEST)
    @Configuration
    @PropertySource("classpath:integration-test.properties")
    static class IntegrationTestProperties {}
}
