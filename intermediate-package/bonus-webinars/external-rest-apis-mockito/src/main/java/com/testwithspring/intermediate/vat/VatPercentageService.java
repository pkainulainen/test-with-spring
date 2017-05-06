package com.testwithspring.intermediate.vat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class VatPercentageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VatPercentageService.class);

    private final RestTemplate restTemplate;

    public VatPercentageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VatPercentage findByCountryCode(String countryCode) {
        LOGGER.info("Finding vat percentage by country code: {}", countryCode);

        VatPercentage vatPercentage = restTemplate.getForObject(String.format(
                "http://localhost:8080/api/external/vat-percentage?countryCode=%s",
                countryCode
            ),
            VatPercentage.class
        );
        LOGGER.info("Found vat percentage: {}", vatPercentage);

        return vatPercentage;
    }
}
