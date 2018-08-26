package com.testwithspring.starter.testdata.constructor;

/**
 * This class demonstrates how we can create the required test data
 * by adding our factory methods to an object mother class.
 *
 * @author Petri Kainulainen
 */
public final class MailingAddressFactory {

    private static final String CITY = "Test City";
    private static final String COUNTRY_SWEDEN = "Sweden";
    private static final String COUNTRY_UNITED_STATES = "USA";
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
     * This factory method takes the information of the created address as method
     * parameters and uses this information when it creates a Finnish address
     * that has a street address.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It takes four method parameters instead of five constructor arguments.</li>
     *     <li>It preserves the connection between our test class and test data.</li>
     * </ul>
     * The cons of this factory method are:
     * <ul>
     *     <li>
     *         It has four method parameters of same type ({@code String} objects).
     *         This might be too much.
     *     </li>
     *     <li>
     *         The order of method parameters is logical only to people who are
     *         familiar with the Finnish address format.
     *     </li>
     * </ul>
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
     * This factory method takes the information of the created address as method
     * parameters and uses this information when it creates a Finnish address
     * that has a post office box.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It takes four method parameters instead of five constructor arguments.</li>
     *     <li>It preserves the connection between our test class and test data.</li>
     * </ul>
     * The cons of this factory method are:
     * <ul>
     *     <li>
     *         It has four method parameters of same type ({@code String} objects).
     *         This might be too much.
     *     </li>
     *     <li>
     *         The order of method parameters is logical only to people who are
     *         familiar with the Finnish address format.
     *     </li>
     * </ul>
     */
    public static MailingAddress createFinnishPostOfficeBoxAddress(String receiver,
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our object mother class when it creates a Finnish address
     * that has a street address.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that the created address
     *         is a Finnish address that has a street address. This works well if
     *         we care only about the fact that the created object contains a
     *         Finnish address that has a street address and we aren't interested in
     *         the actual field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         object mother class. This breaks the connection between our test class
     *         and test data. Also, this factory method forces us to move our
     *         test data to the object mother class that shouldn't be aware of
     *         it. That's why we shouldn't use this factory method if we care
     *         about the field values of the created object.
     *     </li>
     * </ul>
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our object mother class when it creates a Finnish address
     * that has a post office box.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that the created address
     *         is a Finnish address that has a post office box. This works well if
     *         we care only about the fact that the created object contains a
     *         Finnish address that has a post office box and we aren't interested in
     *         the actual field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         object mother class. This breaks the connection between our test class
     *         and test data. Also, this factory method forces us to move our
     *         test data to the object mother class that shouldn't be aware of
     *         it. That's why we shouldn't use this factory method if we care
     *         about the field values of the created object.
     *     </li>
     * </ul>
     */
    public static MailingAddress createFinnishPostOfficeBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    /**
     * This factory method takes the information of the created address as method
     * parameters and uses this information when it creates a Swedish address
     * that has a street address.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It takes four method parameters instead of six constructor arguments.</li>
     *     <li>It preserves the connection between our test class and test data.</li>
     * </ul>
     * The cons of this factory method are:
     * <ul>
     *     <li>
     *         It has four method parameters of same type ({@code String} objects).
     *         This might be too much.
     *     </li>
     *     <li>
     *         The order of method parameters is logical only to people who are
     *         familiar with the Swedish address format.
     *     </li>
     * </ul>
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
     * This factory method takes the information of the created address as method
     * parameters and uses this information when it creates a Swedish address
     * that has a post office box.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It takes four method parameters instead of six constructor arguments.</li>
     *     <li>It preserves the connection between our test class and test data.</li>
     * </ul>
     * The cons of this factory method are:
     * <ul>
     *     <li>
     *         It has four method parameters of same type ({@code String} objects).
     *         This might be too much.
     *     </li>
     *     <li>
     *         The order of method parameters is logical only to people who are
     *         familiar with the Swedish address format.
     *     </li>
     * </ul>
     */
    public static MailingAddress createSwedishPostOfficeBoxAddress(String receiver,
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our object mother class when it creates a Swedish address
     * that has a street address.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that the created address
     *         is a Swedish address that has a street address. This works well if
     *         we care only about the fact that the created object contains a
     *         Swedish address that has a street address and we aren't interested in
     *         the actual field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         object mother class. This breaks the connection between our test class
     *         and test data. Also, this factory method forces us to move our
     *         test data to the object mother class that shouldn't be aware of
     *         it. That's why we shouldn't use this factory method if we care
     *         about the field values of the created object.
     *     </li>
     * </ul>
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our object mother class when it creates a Swedish address
     * that has a post office box.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that the created address
     *         is a Swedish address that has a post office box. This works well if
     *         we care only about the fact that the created object contains a
     *         Swedish address that has a post office box and we aren't interested in
     *         the actual field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         object mother class. This breaks the connection between our test class
     *         and test data. Also, this factory method forces us to move our
     *         test data to the object mother class that shouldn't be aware of
     *         it. That's why we shouldn't use this factory method if we care
     *         about the field values of the created object.
     *     </li>
     * </ul>
     */
    public static MailingAddress createSwedishPostOfficeBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                COUNTRY_SWEDEN
        );
    }

    /**
     * This factory method takes the information of the created address as method
     * parameters and uses this information when it creates a US address
     * that has a street address.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It takes five method parameters instead of seven constructor arguments.</li>
     *     <li>It preserves the connection between our test class and test data.</li>
     * </ul>
     * The cons of this factory method are:
     * <ul>
     *     <li>
     *         It has five method parameters of same type ({@code String} objects).
     *         This is too much.
     *     </li>
     *     <li>
     *         The order of method parameters is logical only to people who are
     *         familiar with the way Finnish people construct addresses.
     *     </li>
     * </ul>
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
     * This factory method takes the information of the created address as method
     * parameters and uses this information when it creates a US address
     * that has a post office box.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>It has a good name.</li>
     *     <li>It takes five method parameters instead of seven constructor arguments.</li>
     *     <li>It preserves the connection between our test class and test data.</li>
     * </ul>
     * The cons of this factory method are:
     * <ul>
     *     <li>
     *         It has five method parameters of same type ({@code String} objects).
     *         This is too much.
     *     </li>
     *     <li>
     *         The order of method parameters is logical only to people who are
     *         familiar with the way Finnish people construct addresses.
     *     </li>
     * </ul>
     */
    public static MailingAddress createUSPostOfficeBoxAddress(String receiver,
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our object mother class when it creates a US address
     * that has a street address.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that the created address
     *         is a US address that has a street address. This works well if
     *         we care only about the fact that the created object contains a
     *         US address that has a street address and we aren't interested in
     *         the actual field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         object mother class. This breaks the connection between our test class
     *         and test data. Also, this factory method forces us to move our
     *         test data to the object mother class that shouldn't be aware of
     *         it. That's why we shouldn't use this factory method if we care
     *         about the field values of the created object.
     *     </li>
     * </ul>
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
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our object mother class when it creates a US address
     * that has a post office box.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that the created address
     *         is a US address that has a post office box. This works well if
     *         we care only about the fact that the created object contains a
     *         US address that has a post office box and we aren't interested in
     *         the actual field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         object mother class. This breaks the connection between our test class
     *         and test data. Also, this factory method forces us to move our
     *         test data to the object mother class that shouldn't be aware of
     *         it. That's why we shouldn't use this factory method if we care
     *         about the field values of the created object.
     *     </li>
     * </ul>
     */
    public static MailingAddress createUSPostOfficeBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY, STATE,
                COUNTRY_UNITED_STATES
        );
    }
}
