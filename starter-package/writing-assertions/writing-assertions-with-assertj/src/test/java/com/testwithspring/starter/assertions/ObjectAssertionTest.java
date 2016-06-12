package com.testwithspring.starter.assertions;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class demonstrates how we can write assertions for "simple" objects by using
 * AssertJ.
 *
 * @author Petri Kainulainen
 */
public class ObjectAssertionTest {

    @Test
    public void objectShouldBeNull() {
        Object actual = null;

        assertThat(actual)
                .overridingErrorMessage("Expected object to be null but was: %s",
                        actual
                )
                .isNull();
    }

    @Test
    public void objectShouldNotBeNull() {
        Object actual = new Object();

        assertThat(actual)
                .overridingErrorMessage("Expected object to be not null but was null")
                .isNotNull();
    }

    @Test
    public void twoIntegersShouldBeEqual() {
        Integer actual = 9;
        Integer expected = 9;

        assertThat(actual)
                .overridingErrorMessage("Expected integer to be: %d but was: %d",
                        expected,
                        actual
                )
                .isEqualByComparingTo(expected);
    }

    @Test
    public void twoIntegersShouldNotBeEqual() {
        Integer actual = 9;
        Integer expected = 41;

        assertThat(actual)
                .overridingErrorMessage("Expected integer to not be: %d but was %d",
                        expected,
                        actual
                )
                .isNotEqualByComparingTo(expected);
    }

    @Test
    public void twoStringsShouldBeEqual() {
        String actual = "foo";
        String expected = "foo";

        assertThat(actual)
                .overridingErrorMessage("Expected string to be: %s but was: %s",
                        expected,
                        actual
                )
                .isEqualTo(expected);
    }

    @Test
    public void twoStringsShouldNotBeEqual() {
        String actual = "foo";
        String expected = "fo1o";

        assertThat(actual)
                .overridingErrorMessage("Expected string to not be: %s but was: %s",
                        expected,
                        actual
                )
                .isNotEqualTo(expected);
    }

    @Test
    public void shouldPointToSameObject() {
        Object actual = new Object();
        Object expected = actual;

        assertThat(actual)
                .overridingErrorMessage("Expected two objects to be the same but they were not")
                .isSameAs(expected);
    }

    @Test
    public void shouldNotPointToSameObject() {
        Object actual = new Object();
        Object expected = new Object();

        assertThat(actual)
                .overridingErrorMessage("Expected to objects to not be the same but they were")
                .isNotSameAs(expected);
    }
}
