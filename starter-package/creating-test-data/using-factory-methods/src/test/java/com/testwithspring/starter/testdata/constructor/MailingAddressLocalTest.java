package com.testwithspring.starter.testdata.constructor;

import org.junit.Test;

/**
 * This test class demonstrates how we can create new {@code MailingAddress} objects by using factory methods.
 * <p>
 * <strong>Note:</strong> The test method names found from this class suck. Do not use similar names in your tests.
 *
 * @author Petri Kainulainen
 */
public class MailingAddressLocalTest {

    private static final String CITY = "Test City";
    private static final String COUNTRY_SWEDEN = "Sweden";
    private static final String COUNTRY_UNITED_STATES = "United States";
    private static final String POST_CODE = "11111";
    private static final String POST_OFFICE_BOX = "PO Box 1";
    private static final String RECEIVER = "Teddy Tester";
    private static final String STATE = "TX";
    private static final String STREET_ADDRESS = "Test Street 51";

    @Test
    public void createFinnishAddressWithLocalMethodWithParameters() {
        MailingAddress finnishAddress = createFinnishAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createFinnishPoBoxAddressWithLocalMethodWithParameters() {
        MailingAddress finnishPoBoxAddress = createFinnishAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    /**
     * This factory method doesn't really improve the situation at all. We should rather
     * just use the new keyword in our test method since this only adds unnecessary
     * complexity into our test class.
     */
    private MailingAddress createFinnishAddress(String receiver,
                                                String streetAddress,
                                                String postOfficeBox,
                                                String postCode,
                                                String city) {
        return new MailingAddress(receiver,
                streetAddress,
                postOfficeBox,
                postCode,
                city
        );
    }

    @Test
    public void createFinnishAddressWithLocalMethodWithoutParameters() {
        MailingAddress finnishAddress = createNormalFinnishAddress();
    }

    /*
     * This method takes no method parameters, but it has a price: using this factory
     * method breaks the connection between our test method and test data. That being said,
     * this method works well if we care only about the fact that the address is a
     * "normal" Finnish address and we are not interested in the property values of other properties.
     */
    private MailingAddress createNormalFinnishAddress() {
        return new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createFinnishPoBoxAddressWithLocalMethodWithoutParameters() {
        MailingAddress finnishPoBoxAddress = createFinnishPoBoxAddress();
    }

    /*
     * This method takes no method parameters, but it has a price: using this factory
     * method breaks the connection between our test method and test data. That being said,
     * this method works well if we care only about the fact that the address is a
     * Finnish PO box address and we are not interested in the property values of other properties.
     */
    private MailingAddress createFinnishPoBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createSwedishAddressWithLocalMethodWithParameters() {
        MailingAddress swedishAddress = createSwedishAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createSwedishPoBoxAddressWithLocalMethodWithParameters() {
        MailingAddress swedishPoBoxAddress = createSwedishAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    /**
     * This doesn't really improve the situation. The only improvement is that
     * this method takes five method parameters instead of six constructor arguments.
     * I think that five method parameters is too much (especially when they are all
     * {@code String} objects.).
     */
    private MailingAddress createSwedishAddress(String receiver,
                                                String streetAddress,
                                                String postOfficeBox,
                                                String postCode,
                                                String city) {
        return new MailingAddress(receiver,
                streetAddress,
                postOfficeBox,
                postCode,
                city,
                COUNTRY_SWEDEN
        );
    }

    @Test
    public void createSwedishAddressWithLocalMethodWithoutParameters() {
        MailingAddress swedishAddress = createNormalSwedishAddress();
    }

    @Test
    public void createSwedishPoBoxAddressWithLocalMethodWithoutParameters() {
        MailingAddress swedishPoBoxAddress = createSwedishPoBoxAddress();
    }

    /**
     * This method takes no method parameters, but it has a price: using this factory
     * method breaks the connection between our test method and test data. That being said,
     * this method works well if we only create about the fact that the created address
     * is a "normal" Swedish address and we don't care about the property values of other properties.
     */
    private MailingAddress createNormalSwedishAddress() {
        return new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY,
                COUNTRY_SWEDEN
        );
    }

    /**
     * This method takes no method parameters, but it has a price: using this factory
     * method breaks the connection between our test method and test data. That being said,
     * this method works well if we only create about the fact that the created address
     * is a Swedish PO box address and we don't care about the property values of other properties.
     */
    private MailingAddress createSwedishPoBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                COUNTRY_SWEDEN
        );
    }

    @Test
    public void createUSAddressWithLocalMethodWithParameters() {
        MailingAddress usAddress = createUSAddress(RECEIVER,
                STREET_ADDRESS,
                POST_CODE,
                null,
                CITY,
                STATE
        );
    }

    @Test
    public void createUSPoBoxAddressWithLocalMethodWithParameters() {
        MailingAddress usPoBoxAddress = createUSAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                STATE
        );
    }

    /**
     * This doesn't really improve the situation. The only improvement is that
     * this method takes six method parameters instead of seven constructor arguments.
     * I think that six method parameters is too much (especially when they are all
     * {@code String} objects.).
     */
    private MailingAddress createUSAddress(String receiver,
                                           String streetAddress,
                                           String postOfficeBox,
                                           String postCode,
                                           String city,
                                           String state) {
        return new MailingAddress(receiver,
                streetAddress,
                postOfficeBox,
                postCode,
                city,
                state,
                COUNTRY_UNITED_STATES
        );
    }

    @Test
    public void createUSAddressWithLocalMethodWithoutParameters() {
        MailingAddress usAddress = createNormalUSAddress();
    }

    @Test
    public void createUSPoBoxAddressWithLocalMethodWithoutParameters() {
        MailingAddress usPoBoxAddressAddress = createUSPoBoxAddress();
    }

    /**
     * This method takes no method parameters, but it has a price: using this factory
     * method breaks the connection between our test method and test data. That being said,
     * this method works well if we only create about the fact that the created address
     * is a "normal" US address and we don't care about the property values of other properties.
     */
    private MailingAddress createNormalUSAddress() {
        return new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY,
                STATE,
                COUNTRY_UNITED_STATES
        );
    }

    /**
     * This method takes no method parameters, but it has a price: using this factory
     * method breaks the connection between our test method and test data. That being said,
     * this method works well if we only create about the fact that the created address
     * is a US PO box address and we don't care about the property values of other properties.
     */
    private MailingAddress createUSPoBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                STATE,
                COUNTRY_UNITED_STATES
        );
    }
}
