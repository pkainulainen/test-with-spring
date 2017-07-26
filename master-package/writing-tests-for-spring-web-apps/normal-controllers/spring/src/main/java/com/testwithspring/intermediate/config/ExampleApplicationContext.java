package com.testwithspring.intermediate.config;

import com.testwithspring.intermediate.common.ConstantDateTimeService;
import com.testwithspring.intermediate.common.CurrentTimeDateTimeService;
import com.testwithspring.intermediate.common.DateTimeService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@ComponentScan({
        "com.testwithspring.intermediate.task",
        "com.testwithspring.intermediate.user"
})
@Import({
        PersistenceContext.class,
        WebMvcContext.class,
        SecurityContext.class
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

    @Profile({Profiles.APPLICATION, Profiles.END_TO_END_TEST})
    @Bean
    DateTimeService currentTimeDateTimeService() {
        return new CurrentTimeDateTimeService();
    }

    @Profile(Profiles.INTEGRATION_TEST)
    @Bean
    DateTimeService constantDateTimeService() {
        return new ConstantDateTimeService();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }
}
