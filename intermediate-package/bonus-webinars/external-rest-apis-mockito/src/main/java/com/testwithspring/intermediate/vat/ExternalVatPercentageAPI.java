package com.testwithspring.intermediate.vat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExternalVatPercentageAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalVatPercentageAPI.class);

    @RequestMapping(value = "/api/external/vat-percentage", method = RequestMethod.GET)
    VatPercentage findByCountryCode(@RequestParam("countryCode") String countryCode) {
        LOGGER.info("Finding vat percentage by country code: {}", countryCode);

        VatPercentage vatPercentage = new VatPercentage();
        vatPercentage.setCountryCode(countryCode);
        vatPercentage.setVatPercentage(23);
        LOGGER.info("Found vat percentage: {}", vatPercentage);

        return vatPercentage;
    }
}
