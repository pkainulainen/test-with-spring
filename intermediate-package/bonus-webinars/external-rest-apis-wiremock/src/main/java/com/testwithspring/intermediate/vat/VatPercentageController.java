package com.testwithspring.intermediate.vat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class VatPercentageController {

    private final VatPercentageService service;

    @Autowired
    VatPercentageController(VatPercentageService service) {
        this.service = service;
    }

    @RequestMapping(value = "/api/vat-percentage", method = RequestMethod.GET)
    VatPercentage findByCountryCode(@RequestParam("countryCode") String countryCode) {
        return service.findByCountryCode(countryCode);
    }
}
