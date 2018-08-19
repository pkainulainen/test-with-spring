package com.testwithspring.starter.testdata.constructor;

import org.junit.Test;

/**
 * This test class demonstrates how we can create new {@code MailingAddress} objects
 * by using an object mother class. The goal of these examples is to demonstrate the
 * differences and similarities of factory methods and constructors which use the
 * telescoping constructor antipattern.
 *
 * @author Petri Kainulainen
 */
public class MailingAddressObjectMotherTest {

    private static final String CITY = "Test City";
    private static final String POST_CODE = "11111";
    private static final String POST_OFFICE_BOX = "PO Box 1";
    private static final String RECEIVER = "Teddy Tester";
    private static final String STATE = "TX";
    private static final String STREET_ADDRESS = "Test Street 51";

    @Test
    public void createFinnishAddressWithObjectMotherMethodWithParameters() {
        MailingAddress finnishAddress = MailingAddressFactory.createNormalFinnishAddress(RECEIVER,
                STREET_ADDRESS,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createFinnishPoBoxAddressWithObjectMotherMethodWithParameters() {
        MailingAddress finnishPoBoxAddress = MailingAddressFactory.createFinnishPostOfficeBoxAddress(RECEIVER,
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
        MailingAddress finnishPoBoxAddress = MailingAddressFactory.createFinnishPostOfficeBoxAddress();
    }

    @Test
    public void createSwedishAddressWithObjectMotherMethodWithParameters() {
        MailingAddress swedishAddress = MailingAddressFactory.createNormalSwedishAddress(RECEIVER,
                STREET_ADDRESS,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createSwedishPoBoxAddressWithObjectMotherMethodWithParameters() {
        MailingAddress swedishPoBoxAddress = MailingAddressFactory.createSwedishPostOfficeBoxAddress(RECEIVER,
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
        MailingAddress swedishPoBoxAddress = MailingAddressFactory.createSwedishPostOfficeBoxAddress();
    }

    @Test
    public void createUSAddressWithObjectMotherMethodWithParameters() {
        MailingAddress usAddress = MailingAddressFactory.createNormalUSAddress(RECEIVER,
                STREET_ADDRESS,
                POST_CODE,
                CITY,
                STATE
        );
    }

    @Test
    public void createUSPoBoxAddressWithObjectMotherMethodWithParameters() {
        MailingAddress usPoBoxAddress = MailingAddressFactory.createUSPostOfficeBoxAddress(RECEIVER,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                STATE
        );
    }

    @Test
    public void createUSAddressWithObjectMotherMethodWithoutParameters() {
        MailingAddress usAddress = MailingAddressFactory
                .createNormalUSAddress();
    }

    @Test
    public void createUSPoBoxAddressWithObjectMotherWithoutParameters() {
        MailingAddress usPoBoxAddress = MailingAddressFactory.createUSPostOfficeBoxAddress();
    }
}
