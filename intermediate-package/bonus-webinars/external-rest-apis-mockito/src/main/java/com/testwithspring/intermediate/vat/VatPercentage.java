package com.testwithspring.intermediate.vat;

public class VatPercentage {

    private String countryCode;
    private Integer vatPercentage;

    public VatPercentage() {}

    public String getCountryCode() {
        return countryCode;
    }

    public Integer getVatPercentage() {
        return vatPercentage;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setVatPercentage(Integer vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    @Override
    public String toString() {
        return "VatPercentage{" +
                "countryCode='" + countryCode + '\'' +
                ", vatPercentage=" + vatPercentage +
                '}';
    }
}
