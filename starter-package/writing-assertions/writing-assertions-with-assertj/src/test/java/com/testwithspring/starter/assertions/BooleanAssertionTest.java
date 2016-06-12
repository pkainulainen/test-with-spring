package com.testwithspring.starter.assertions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(TRUE)
                .overridingErrorMessage("Expected boolean to be true but was false")
                .isTrue();
    }

    @Test
    public void booleanShouldBeFalse() {
       assertThat(FALSE)
               .overridingErrorMessage("Expected boolean to be false but was true")
               .isFalse();
    }
}
