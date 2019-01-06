package com.testwithspring.starter.testdata.constructor;

import org.junit.Test;

/**
 * This test class demonstrates how we can create new {@code MailingAddress}
 * objects by using a test data builder. The goal of this lesson is prove
 * that a test data builder can help us to write clean tests when our
 * test code requires objects which are created by using the telescoping
 * constructor antipattern.
 *
 * @author Petri Kainulainen
 */
public class MailingAddressTest {

    private static final String CITY = "Test City";
    private static final String COUNTRY_SWEDEN = "Sweden";
    private static final String COUNTRY_UNITED_STATES = "USA";
    private static final String POST_CODE = "11111";
    private static final String POST_OFFICE_BOX = "PO Box 1";
    private static final String RECEIVER = "Teddy Tester";
    private static final String STATE = "TX";
    private static final String STREET_ADDRESS = "Test Street 51";

    @Test
    public void createFinnishAddress() {
        MailingAddress finnishAddress = new MailingAddressBuilder()
                .withReceiver(RECEIVER)
                .withStreetAddress(STREET_ADDRESS)
                .withPostCode(POST_CODE)
                .withCity(CITY)
                .build();
    }

    @Test
    public void createFinnishPoBoxAddress() {
        MailingAddress finnishPoBoxAddress = new MailingAddressBuilder()
                .withReceiver(RECEIVER)
                .withPostOfficeBox(POST_OFFICE_BOX)
                .withPostCode(POST_CODE)
                .withCity(CITY)
                .build();
    }

    @Test
    public void createSwedishAddress() {
        MailingAddress swedishAddress = new MailingAddressBuilder()
                .withReceiver(RECEIVER)
                .withStreetAddress(STREET_ADDRESS)
                .withPostCode(POST_CODE)
                .withCity(CITY)
                .withCountry(COUNTRY_SWEDEN)
                .build();
    }

    @Test
    public void createSwedishPoBoxAddress() {
        MailingAddress swedishPoBoxAddress = new MailingAddressBuilder()
                .withReceiver(RECEIVER)
                .withPostOfficeBox(POST_OFFICE_BOX)
                .withPostCode(POST_CODE)
                .withCity(CITY)
                .withCountry(COUNTRY_SWEDEN)
                .build();
    }

    @Test
    public void createUSAddress() {
        MailingAddress usAddress = new MailingAddressBuilder()
                .withReceiver(RECEIVER)
                .withStreetAddress(STREET_ADDRESS)
                .withPostCode(POST_CODE)
                .withCity(CITY)
                .withState(STATE)
                .withCountry(COUNTRY_UNITED_STATES)
                .build();
    }

    @Test
    public void createUSPoBoxAddress() {
        MailingAddress usPoBoxAddress = new MailingAddressBuilder()
                .withReceiver(RECEIVER)
                .withPostOfficeBox(POST_OFFICE_BOX)
                .withPostCode(POST_CODE)
                .withCity(CITY)
                .withState(STATE)
                .withCountry(COUNTRY_UNITED_STATES)
                .build();
    }
}
