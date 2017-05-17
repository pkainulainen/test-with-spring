package com.testwithspring.intermediate;

import com.testwithspring.intermediate.common.AuditingDateTimeProvider;
import com.testwithspring.intermediate.common.ConstantDateTimeService;
import com.testwithspring.intermediate.common.CurrentTimeDateTimeService;
import com.testwithspring.intermediate.common.DateTimeService;
import com.testwithspring.intermediate.config.Profiles;
import com.testwithspring.intermediate.config.SecurityContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@Import(SecurityContext.class)
public class TaskTrackerApplication {

    public static final Locale LOCALE = Locale.ENGLISH;

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
    DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }

    @Profile({Profiles.INTEGRATION_TEST, Profiles.END_TO_END_TEST})
    @Bean
    LocaleResolver fixedLocaleResolver() {
        return new FixedLocaleResolver(LOCALE);
    }

    @Profile(Profiles.APPLICATION)
    @Bean
    LocaleResolver localeResolver() {
        return new CookieLocaleResolver();
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }
}
