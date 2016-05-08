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
    private MailingAddressFactory() {
    }

    /**
     * This method improves the situation because it is named properly and
     * it takes four method parameters instead of five constructor arguments.
     * Although this is a somewhat impressive, I think that for method
     * parameters might be too much (especially when they are all {@code String} objects).
     *
     * However, if we would have to create only Finnish and Swedish addresses, I would probably use this
     * method because the context helps anyone to understand to order of these arguments.
     */
    public static MailingAddress createNormalFinnishAddress(String receiver,
                                                            String streetAddress,
                                                            String postCode,
                                                            String city) {
        return new MailingAddress(receiver,
                streetAddress,
                null,
                postCode,
                city
        );
    }

    /**
     * This method improves the situation because it is named properly and
     * it takes four method parameters instead of five constructor arguments.
     * Although this is a somewhat impressive, I think that for method
     * parameters might be too much (especially when they are all {@code String} objects)
     * .
     * However, if we would have to create only Finnish and Swedish addresses, I would probably use this
     * method because the context helps anyone to understand to order of these arguments.
     */
    public static MailingAddress createFinnishPoBoxAddress(String receiver,
                                                           String postOfficeBox,
                                                           String postCode,
                                                           String city) {
        return new MailingAddress(receiver,
                null,
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
     * This method improves the situation because it is named properly and
     * it takes four method parameters instead of six constructor arguments.
     * Although this is a somewhat impressive, I think that for method
     * parameters might be too much (especially when they are all {@code String} objects).
     *
     * However, if we would have to create only Finnish and Swedish addresses, I would probably use this
     * method because the context helps anyone to understand to order of these arguments.
     */
    public static MailingAddress createNormalSwedishAddress(String receiver,
                                                            String streetAddress,
                                                            String postCode,
                                                            String city) {
        return new MailingAddress(receiver,
                streetAddress,
                null,
                postCode,
                city,
                COUNTRY_SWEDEN
        );
    }

    /**
     * This method improves the situation because it is named properly and
     * it takes four method parameters instead of six constructor arguments.
     * Although this is a somewhat impressive, I think that for method
     * parameters might be too much (especially when they are all {@code String} objects).
     *
     * However, if we would have to create only Finnish and Swedish addresses, I would probably use this
     * method because the context helps anyone to understand to order of these arguments.
     */
    public static MailingAddress createSwedishPoBoxAddress(String receiver,
                                                    String postOfficeBox,
                                                    String postCode,
                                                    String city) {
        return new MailingAddress(receiver,
                null,
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
     * <p>
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
     * <p>
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
     * This method improves the situation because it is named properly and
     * it takes five method parameters instead of seven constructor arguments.
     * Although this is a somewhat impressive, I think that five method
     * parameters is too much (especially when they are all {@code String} objects).
     */
    public static MailingAddress createNormalUSAddress(String receiver,
                                                 String streetAddress,
                                                 String postCode,
                                                 String city,
                                                 String state) {
        return new MailingAddress(receiver,
                streetAddress,
                null,
                postCode,
                city,
                state,
                COUNTRY_UNITED_STATES
        );
    }

    /**
     * This method improves the situation because it is named properly and
     * it takes five method parameters instead of seven constructor arguments.
     * Although this is a somewhat impressive, I think that five method
     * parameters is too much (especially when they are all {@code String} objects).
     */
    public static MailingAddress createUSPoBoxAddress(String receiver,
                                                String postOfficeBox,
                                                String postCode,
                                                String city,
                                                String state) {
        return new MailingAddress(receiver,
                null,
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
     * <p>
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
     * <p>
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
