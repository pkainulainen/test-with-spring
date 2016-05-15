package com.testwithspring.starter.testdata.constructor;

import org.junit.Test;

/**
 * This test class demonstrates the problems which we face when we create
 * new instances of a class that uses the telescoping constructor anti-pattern.
 *
 * <strong>Note:</strong> The test method names found from this class suck. Do not use similar names in your tests.
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

    /**
     * This constructor has two problems:
     * <ul>
     *     <li>It takes five String objects as constructor arguments.</li>
     *     <li>The second or the third constructor argument must always be null.</li>
     * </ul>
     */
    @Test
    public void createFinnishAddress() {
        MailingAddress finnishAddress = new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY
        );
    }

    /**
     * This constructor has two problems:
     * <ul>
     *     <li>It takes five String objects as constructor arguments.</li>
     *     <li>The second or the third constructor argument must always be null.</li>
     * </ul>
     */
    @Test
    public void createFinnishPoBoxAddress() {
        MailingAddress finnishPoBoxAddress = new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY
        );
    }

    /**
     * This constructor has three problems:
     * <ul>
     *     <li>It takes six String objects as constructor arguments.</li>
     *     <li>The second or third argument must always be null.</li>
     *     <li>We can use this constructor for creating Finnish mailing addresses.</li>
     * </ul>
     */
    @Test
    public void createSwedishAddress() {
        MailingAddress swedishAddress = new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY,
                COUNTRY_SWEDEN
        );
    }

    /**
     * This constructor has three problems:
     * <ul>
     *     <li>It takes six String objects as constructor arguments.</li>
     *     <li>The second or third argument must always be null.</li>
     *     <li>We can use this constructor for creating Finnish mailing addresses.</li>
     * </ul>
     */
    @Test
    public void createSwedishPoBoxAddress() {
        MailingAddress swedishPoBoxAddress = new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                COUNTRY_SWEDEN
        );
    }

    /**
     * This constructor has four problems:
     * <ul>
     *     <li>It takes seven String objects as constructor arguments.</li>
     *     <li>The second or third argument must always be null.</li>
     *     <li>We can use this constructor for creating Finnish or Swedish addresses as well.</li>
     *     <li>The order of constructor arguments makes sense only if the developer is from Finland.</li>
     * </ul>
     */
    @Test
    public void createUSAddress() {
        MailingAddress usAddress = new MailingAddress(RECEIVER,
                STREET_ADDRESS,
                null,
                POST_CODE,
                CITY,
                STATE,
                COUNTRY_UNITED_STATES
        );
    }

    /**
     * This constructor has four problems:
     * <ul>
     *     <li>It takes seven String objects as constructor arguments.</li>
     *     <li>The second or third argument must always be null.</li>
     *     <li>We can use this constructor for creating Finnish or Swedish addresses as well.</li>
     *     <li>The order of constructor arguments makes sense only if the developer is from Finland.</li>
     * </ul>
     */
    @Test
    public void createUSPoBoxAddress() {
        MailingAddress usPoBoxAddress = new MailingAddress(RECEIVER,
                null,
                POST_OFFICE_BOX,
                POST_CODE,
                CITY,
                STATE,
                COUNTRY_UNITED_STATES
        );
    }
}
