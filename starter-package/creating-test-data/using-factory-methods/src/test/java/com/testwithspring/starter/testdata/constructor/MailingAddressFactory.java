package com.testwithspring.starter.testdata.constructor;

/**
 * This class demonstrates how we can create a so called object mother class that is used for creating
 * objects that are required by our tests.
 *
 * @author Petri Kainulainen
 */
public final class MailingAddressFactory {

    private static final String CITY = "Test City";
    private static final String COUNTRY_SWEDEN = "Sweden";
    private static final String COUNTRY_UNITED_STATES = "United States";
    private static final String POST_CODE = "11111";
    private static final String POST_OFFICE_BOX = "PO Box 1";
    private static final String RECEIVER = "Teddy Tester";
    private static final String STATE = "TX";
    private static final String STREET_ADDRESS = "Test Street 51";

    /**
     * Prevents instantiation.
     */
    private MailingAddressFactory() {}

    /**
     * This factory method doesn't really improve the situation at all. We should rather
     * just use the new keyword in our test method since this only adds unnecessary
     * complexity into our test suite
     */
    public static MailingAddress createFinnishAddress(String receiver,
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

    /**
     * Using a factory method like this is a horrible idea because it breaks
     * the connection of the test method and the test data. The worst part is
     * that this forces us to move the test data into the object mother class
     * that shouldn't be aware of it.
     *
     * That being said, this method works well if we only create about the fact
     * that the created address is a normal Finnish address and we don't care about
     * the property values of other properties.
     */
    public static MailingAddress createNormalFinnishAddress() {
        return new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY
        );
    }

    /**
     * Using a factory method like this is a horrible idea because it breaks
     * the connection of the test method and the test data. The worst part is
     * that this forces us to move the test data into the object mother class
     * that shouldn't be aware of it.
     *
     * That being said, this method works well if we only create about the fact
     * that the created address is a Finnish PO box address and we don't care about
     * the property values of other properties.
     */
    public static MailingAddress createFinnishPoBoxAddress() {
        return new MailingAddress(RECEIVER,
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
    public static MailingAddress createSwedishAddress(String receiver,
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

    /**
     * Using a factory method like this is a horrible idea because it breaks
     * the connection of the test method and the test data. The worst part is
     * that this forces us to move the test data into the object mother class
     * that shouldn't be aware of it.
     *
     * That being said, this method works well if we only create about the fact
     * that the created address is a normal Swedish address and we don't care about
     * the property values of other properties.
     */
    public static MailingAddress createNormalSwedishAddress() {
        return new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY,
                COUNTRY_SWEDEN
        );
    }

    /**
     * Using a factory method like this is a horrible idea because it breaks
     * the connection of the test method and the test data. The worst part is
     * that this forces us to move the test data into the object mother class
     * that shouldn't be aware of it.
     *
     * That being said, this method works well if we only create about the fact
     * that the created address is a Swedish PO box address and we don't care about
     * the property values of other properties.
     */
    public static MailingAddress createSwedishPoBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                COUNTRY_SWEDEN
        );
    }

    /**
     * This doesn't really improve the situation. The only improvement is that
     * this method takes six method parameters instead of seven constructor arguments.
     * I think that six method parameters is too much (especially when they are all
     * {@code String} objects.).
     */
    public static MailingAddress createUSAddress(String receiver,
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

    /**
     * Using a factory method like this is a horrible idea because it breaks
     * the connection of the test method and the test data. The worst part is
     * that this forces us to move the test data into the object mother class
     * that shouldn't be aware of it.
     *
     * That being said, this method works well if we only care about the fact that
     * the returned object is a normal US address and we don't care about the
     * property values of other properties.
     */
    public static MailingAddress createNormalUSAddress() {
        return new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY, STATE,
                COUNTRY_UNITED_STATES
        );
    }

    /**
     * Using a factory method like this is a horrible idea because it breaks
     * the connection of the test method and the test data. The worst part is
     * that this forces us to move the test data into the object mother class
     * that shouldn't be aware of it.
     *
     * That being said, this method works well if we only care about the fact that
     * the returned object is a US PO box address and we don't care about the
     * property values of other properties.
     */
    public static MailingAddress createUSPoBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY, STATE,
                COUNTRY_UNITED_STATES
        );
    }
}
