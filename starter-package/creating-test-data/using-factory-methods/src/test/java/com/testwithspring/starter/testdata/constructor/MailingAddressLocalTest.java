package com.testwithspring.starter.testdata.constructor;

import org.junit.Test;

/**
 * This test class demonstrates how we can create new {@code MailingAddress} objects
 * by using local factory methods. The goal of these examples is to demonstrate the
 * differences and similarities of factory methods and constructors which use the
 * telescoping constructor antipattern.
 *
 * @author Petri Kainulainen
 */
public class MailingAddressLocalTest {

    private static final String CITY = "Test City";
    private static final String COUNTRY_SWEDEN = "Sweden";
    private static final String COUNTRY_UNITED_STATES = "USA";
    private static final String POST_CODE = "11111";
    private static final String POST_OFFICE_BOX = "PO Box 1";
    private static final String RECEIVER = "Teddy Tester";
    private static final String STATE = "TX";
    private static final String STREET_ADDRESS = "Test Street 51";

    @Test
    public void createFinnishAddressWithLocalMethodWithParameters() {
        MailingAddress finnishAddress = createNormalFinnishAddress(RECEIVER,
                STREET_ADDRESS,
                POST_CODE,
                CITY
        );
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
     *     <li>It preserves the connection between our test method and test data.</li>
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
    private MailingAddress createNormalFinnishAddress(String receiver,
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

    @Test
    public void createFinnishPoBoxAddressWithLocalMethodWithParameters() {
        MailingAddress finnishPoBoxAddress = createFinnishPostOfficeBoxAddress(RECEIVER,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
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
     *     <li>It preserves the connection between our test method and test data.</li>
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

    private MailingAddress createFinnishPostOfficeBoxAddress(String receiver,
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

    @Test
    public void createFinnishAddressWithLocalMethodWithoutParameters() {
        MailingAddress finnishAddress = createNormalFinnishAddress();
    }

    /**
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates a Finnish address
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
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
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
        MailingAddress finnishPoBoxAddress = createFinnishPostOfficeBoxAddress();
    }

    /**
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates a Finnish address
     * that has a post office box.
     *
     * The pros of this factory method are:
     * <ul>
     *     <li>
     *         It has a good name that emphasizes the fact that the created address
     *         is a Finnish address that has a post office box. This works well
     *         if we care only about the fact that the created object contains a
     *         Finnish address that has a post office box and we aren't
     *         interested in the actual field values of the created object.
     *     </li>
     * </ul>
     * The cons of this method are:
     * <ul>
     *     <li>
     *         Because it has no method parameters, it sets the field values
     *         of the created object by using the constants declared by our
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
     */
    private MailingAddress createFinnishPostOfficeBoxAddress() {
        return new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    @Test
    public void createSwedishAddressWithLocalMethodWithParameters() {
        MailingAddress swedishAddress = createNormalSwedishAddress(RECEIVER,
                STREET_ADDRESS,
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
     *     <li>It preserves the connection between our test method and test data.</li>
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
    private MailingAddress createNormalSwedishAddress(String receiver,
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

    @Test
    public void createSwedishPoBoxAddressWithLocalMethodWithParameters() {
        MailingAddress swedishPoBoxAddress = createSwedishPostOfficeBoxAddress(RECEIVER,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
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
     *     <li>It preserves the connection between our test method and test data.</li>
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
    private MailingAddress createSwedishPostOfficeBoxAddress(String receiver,
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

    @Test
    public void createSwedishAddressWithLocalMethodWithoutParameters() {
        MailingAddress swedishAddress = createNormalSwedishAddress();
    }

    /**
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates a Swedish address
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
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
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

    @Test
    public void createSwedishPoBoxAddressWithLocalMethodWithoutParameters() {
        MailingAddress swedishPoBoxAddress = createSwedishPostOfficeBoxAddress();
    }

    /**
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates a Swedish address
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
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
     */
    private MailingAddress createSwedishPostOfficeBoxAddress() {
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
        MailingAddress usAddress = createNormalUSAddress(RECEIVER,
                STREET_ADDRESS,
                POST_CODE,
                CITY,
                STATE
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
     *     <li>It preserves the connection between our test method and test data.</li>
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
    private MailingAddress createNormalUSAddress(String receiver,
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

    @Test
    public void createUSPoBoxAddressWithLocalMethodWithParameters() {
        MailingAddress usPoBoxAddress = createUSPostOfficeBoxAddress(RECEIVER,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                STATE
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
    private MailingAddress createUSPostOfficeBoxAddress(String receiver,
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

    @Test
    public void createUSAddressWithLocalMethodWithoutParameters() {
        MailingAddress usAddress = createNormalUSAddress();
    }

    /**
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates a US address
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
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
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

    @Test
    public void createUSPoBoxAddressWithLocalMethodWithoutParameters() {
        MailingAddress usPoBoxAddressAddress = createUSPostOfficeBoxAddress();
    }

    /**
     * This factory method has no method parameters. Instead, it will use the
     * constants defined in our test class when it creates a US address
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
     *         test class. This breaks the connection between our test method
     *         and test data.
     *     </li>
     * </ul>
     */
    private MailingAddress createUSPostOfficeBoxAddress() {
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
