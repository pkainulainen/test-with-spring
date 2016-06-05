package com.testwithspring.starter.assertions;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * This test class demonstrates how we can write assertions for conditions that
 * can be either true or false.
 *
 * @author Petri Kainulainen
 */
public class BooleanAssertionTest {

    private static final boolean FALSE = false;
    private static final boolean TRUE = true;

    @Test
    public void booleanShouldBeTrue() {
        assertThat(TRUE, is(true));
        assertThat("Expected boolean to be true but was false", TRUE, is(true));
    }

    @Test
    public void booleanShouldBeFalse() {
        assertThat(FALSE, is(false));
        assertThat("Expected boolean to be false but was true", FALSE, is(false));
    }
}
