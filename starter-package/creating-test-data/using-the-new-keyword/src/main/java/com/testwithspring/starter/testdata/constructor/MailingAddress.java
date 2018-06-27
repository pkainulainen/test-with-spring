package com.testwithspring.starter.testdata.constructor;

/**
 * Contains the information of a single mailing address.
 *
 * @author Petri Kainulainen
 */
public class MailingAddress {

    private final String receiver;
    private final String streetAddress;
    private final String postOfficeBox;
    private final String postCode;
    private final String city;
    private final String state;
    private final String country;

    /**
     * Creates a new <code>MailingAddress</code> object. This constructor is used when we
     * send shipments (letters or packages) to a Finnish address.
     *
     *<strong>Note:</strong> We need to specify either street address or post office box.
     *
     * @param receiver
     * @param streetAddress
     * @param postOfficeBox
     * @param postCode
     * @param city
     */
    public MailingAddress(String receiver,
                          String streetAddress,
                          String postOfficeBox,
                          String postCode,
                          String city) {
        this(receiver, streetAddress, postOfficeBox, postCode, city, null);
    }

    /**
     * Creates a new <code>MailingAddress</code> object. This constructor is used when we
     * send shipments (letters or packages) to another country that doesn't require the
     * state information (such as Sweden).
     *
     * <strong>Note:</strong> We need to specify either street address or post office box.
     *
     * @param receiver
     * @param streetAddress
     * @param postOfficeBox
     * @param postCode
     * @param city
     * @param country
     */
    public MailingAddress(String receiver,
                          String streetAddress,
                          String postOfficeBox,
                          String postCode,
                          String city,
                          String country) {
        this(receiver, streetAddress, postOfficeBox, postCode, city, null, country);
    }

    /**
     * Creates a new <code>MailingAddress</code> object. This constructor is used when we send
     * shipments (letters or packages) to a country that requires the state information
     * (such as United States).
     *
     * <strong>Note:</strong> We need to specify either street address or post office box.
     *
     * @param receiver
     * @param streetAddress
     * @param postOfficeBox
     * @param postCode
     * @param city
     * @param state
     * @param country
     */
    public MailingAddress(String receiver,
                          String streetAddress,
                          String postOfficeBox,
                          String postCode,
                          String city,
                          String state,
                          String country) {
        this.receiver = receiver;
        this.streetAddress = streetAddress;
        this.postOfficeBox = postOfficeBox;
        this.postCode = postCode;
        this.city = city;
        this.state = state;
        this.country = country;
    }
}
