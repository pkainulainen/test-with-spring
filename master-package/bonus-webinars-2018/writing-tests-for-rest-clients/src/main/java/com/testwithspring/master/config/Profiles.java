package com.testwithspring.master.config;

/**
 * Declares the available Spring profiles.
 */
public final class Profiles {

    public static final String APPLICATION = "application";
    public static final String END_TO_END_TEST = "endToEndTest";
    public static final String INTEGRATION_TEST = "integrationTest";
    public static final String UNIT_TEST = "unitTest";

    private Profiles() {}
}
