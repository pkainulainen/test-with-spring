package com.testwithspring.intermediate;

import com.testwithspring.intermediate.vat.VatPercentage;
import com.testwithspring.intermediate.vat.VatPercentageService;

/**
 *
 */
public class VatPercentageServiceStub extends VatPercentageService {

    private static final Integer VAT_PERCENTAGE = 24;

    VatPercentageServiceStub() {
        super(null);
    }

    @Override
    public VatPercentage findByCountryCode(String countryCode) {
        VatPercentage vatPercentage = new VatPercentage();
        vatPercentage.setCountryCode(countryCode);
        vatPercentage.setVatPercentage(VAT_PERCENTAGE);

        return vatPercentage;
    }
}
