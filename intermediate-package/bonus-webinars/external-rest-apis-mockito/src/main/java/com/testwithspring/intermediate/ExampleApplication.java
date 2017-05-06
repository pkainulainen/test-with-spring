package com.testwithspring.intermediate;

import com.testwithspring.intermediate.vat.VatPercentageService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ExampleApplication {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Profile(Profiles.APPLICATION)
    VatPercentageService vatPercentageService(RestTemplate restTemplate) {
        return new VatPercentageService(restTemplate);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
