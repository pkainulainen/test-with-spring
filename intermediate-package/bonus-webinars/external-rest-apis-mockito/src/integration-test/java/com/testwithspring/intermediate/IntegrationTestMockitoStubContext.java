package com.testwithspring.intermediate;

import com.testwithspring.intermediate.vat.VatPercentageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import static com.testwithspring.intermediate.TestDoubles.stub;

@Configuration
@Import({ExampleApplication.class})
public class IntegrationTestMockitoStubContext {

    @Bean
    @Profile(Profiles.INTEGRATION_TEST)
    VatPercentageService vatPercentageService() {
        return stub(VatPercentageService.class);
    }
}
