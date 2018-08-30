package com.testwithspring.master;

import com.testwithspring.master.common.AuditingDateTimeProvider;
import com.testwithspring.master.common.ConstantDateTimeService;
import com.testwithspring.master.common.CurrentTimeDateTimeService;
import com.testwithspring.master.common.DateTimeService;
import com.testwithspring.master.config.Profiles;
import com.testwithspring.master.config.SecurityContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@Import(SecurityContext.class)
public class TaskTrackerApplication {

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

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }
}
