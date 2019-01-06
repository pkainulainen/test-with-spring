package com.testwithspring.starter.testdata.constructor;

/**
 * This class demonstrates how we can create a simple test data builder
 * that can be used for creating new {@code MailingAddress} objects.
 *
 * @author Petri Kainulainen
 */
public final class MailingAddressBuilder {

    private String receiver;
    private String streetAddress;
    private String postOfficeBox;
    private String postCode;
    private String city;
    private String state;
    private String country;

    public MailingAddressBuilder() {

    }

    public MailingAddressBuilder withReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public MailingAddressBuilder withStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public MailingAddressBuilder withPostOfficeBox(String postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
        return this;
    }

    public MailingAddressBuilder withPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

    public MailingAddressBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public MailingAddressBuilder withState(String state) {
        this.state = state;
        return this;
    }

    public MailingAddressBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public MailingAddress build() {
        return new MailingAddress(receiver,
                streetAddress,
                postOfficeBox,
                postCode,
                city,
                state,
                country);
    }
}
