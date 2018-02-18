package com.testwithspring.master;

/**
 * Specifies the test types that used to group different
 * tests into different groups that can be run individually.
 */
public final class TestTags {

    /**
     * Identifies tests which should be run when we run our
     * end-to-end tests.
     */
    public static final String END_TO_END_TEST = "EndToEndTest";

    /**
     * Identifies tests which should be run when we run our
     * integration tests.
     */
    public static final String INTEGRATION_TEST = "IntegrationTest";

    /**
     * Identifies tests which should be run when we run
     * our unit tests.
     */
    public static final String UNIT_TEST = "UnitTest";

    /**
     * Prevents instantiation.
     */
    private TestTags() {}
}
