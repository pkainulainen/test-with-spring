package com.testwithspring.intermediate.vat;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VatPercentageService {

    private final RestTemplate restTemplate;

    public VatPercentageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VatPercentage findByCountryCode(String countryCode) {
        return restTemplate.getForObject(String.format(
                "http://localhost:8080/api/external/vat-percentage?countryCode=%s",
                countryCode
            ),
            VatPercentage.class
        );
    }
}
