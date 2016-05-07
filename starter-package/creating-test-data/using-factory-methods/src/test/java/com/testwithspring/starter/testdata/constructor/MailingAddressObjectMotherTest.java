package com.testwithspring.starter.testdata.constructor;

import org.junit.Test;

public class MailingAddressObjectMotherTest {

    private static final String CITY = "Test City";
    private static final String COUNTRY_SWEDEN = "Sweden";
    private static final String COUNTRY_UNITED_STATES = "United States";
    private static final String POST_CODE = "11111";
    private static final String POST_OFFICE_BOX = "PO Box 1";
    private static final String RECEIVER = "Teddy Tester";
    private static final String STATE = "TX";
    private static final String STREET_ADDRESS = "Test Street 51";

    @Test
    public void createFinnishAddressWithObjectMotherMethodWithParameters() {
        MailingAddress finnishAddress = MailingAddressFactory.createFinnishAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createFinnishPoBoxAddressWithObjectMotherMethodWithParameters() {
        MailingAddress finnishPoBoxAddress = MailingAddressFactory.createFinnishAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createFinnishAddressWithLocalMethodWithoutParameters() {
        MailingAddress finnishAddress = MailingAddressFactory.createNormalFinnishAddress();
    }

    @Test
    public void createFinnishPoBoxAddressWithObjectMotherMethodWithoutParameters() {
        MailingAddress finnishPoBoxAddress = MailingAddressFactory.createFinnishPoBoxAddress();
    }

    @Test
    public void createFinnishPoBoxAddressWithLocalMethodWithParameters() {
        MailingAddress finnishPoBoxAddress = MailingAddressFactory.createFinnishAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createSwedishAddressWithObjectMotherMethodWithParameters() {
        MailingAddress swedishAddress = MailingAddressFactory.createSwedishAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createSwedishPoBoxAddressWithObjectMotherMethodWithParameters() {
        MailingAddress swedishPoBoxAddress = MailingAddressFactory.createSwedishAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createSwedishAddressWithLocalMethodWithoutParameters() {
        MailingAddress swedishAddress = MailingAddressFactory.createNormalSwedishAddress();
    }

    @Test
    public void createSwedishPoBoxAddressWithLocalMethodWithoutParameters() {
        MailingAddress swedishPoBoxAddress = MailingAddressFactory.createSwedishPoBoxAddress();
    }

    @Test
    public void createUSAddressWithObjectMotherMethodWithParameters() {
        MailingAddress usAddress = MailingAddressFactory.createUSAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY,
                STATE
        );
    }

    @Test
    public void createUSPoBoxAddressWithObjectMotherMethodWithParameters() {
        MailingAddress usPoBoxAddress = MailingAddressFactory.createUSAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                STATE
        );
    }

    @Test
    public void createUSAddressWithObjectMotherMethodWithoutParameters() {
        MailingAddress usAddress = MailingAddressFactory.createNormalUSAddress();
    }

    @Test
    public void createUSPoBoxAddressWithObjectMotherWithoutParameters() {
        MailingAddress usPoBoxAddress = MailingAddressFactory.createUSPoBoxAddress();
    }
}
