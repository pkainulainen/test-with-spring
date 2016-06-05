package com.testwithspring.starter.assertions;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

/**
 * This test class demonstrates how we can write assertions for "simple" objects by using
 * JUnit 4 and Hamcrest.
 *
 * @author Petri Kainulainen
 */
public class ObjectAssertionTest {

    @Test
    public void objectShouldBeNull() {
        Object actual = null;

        assertThat(actual, nullValue());
        assertThat(String.format("Expected object to be null but was: %s", actual),
                actual,
                nullValue()
        );
    }

    @Test
    public void objectShouldNotBeNull() {
        Object actual = new Object();

        assertThat(actual, notNullValue());
        assertThat("Expected object to be not null but was null", actual, notNullValue());
    }

    @Test
    public void twoIntegersShouldBeEqual() {
        Integer actual = 9;
        Integer expected = 9;

        assertThat(actual, is(expected));
        assertThat(String.format("Expected integer to be: %d but was: %d", expected, actual),
                actual,
                is(expected)
        );
    }

    @Test
    public void twoIntegersShouldNotBeEqual() {
        Integer actual = 9;
        Integer expected = 41;

        assertThat(actual, not(expected));
        assertThat(String.format("Expected integer to not be: %d but was: %d", expected, actual),
                actual,
                not(expected)
        );
    }

    @Test
    public void twoStringsShouldBeEqual() {
        String actual = "foo";
        String expected = "foo";

        assertThat(actual, is(expected));
        assertThat(String.format("Expected string to be: %s but was: %s", expected, actual),
                actual,
                is(expected)
        );
    }

    @Test
    public void twoStringsShouldNotBeEqual() {
        String actual = "foo";
        String expected = "fo1o";

        assertThat(actual, not(expected));
        assertThat(String.format("Expected string to not be: %s but was: %s", expected, expected),
                actual,
                not(expected)
        );
    }

    @Test
    public void shouldPointToSameObject() {
        Object actual = new Object();
        Object expected = actual;

        assertThat(actual, sameInstance(expected));
        assertThat("Expected two objects to be the same but they are not",
                actual,
                sameInstance(expected)
        );
    }

    @Test
    public void shouldNotPointToSameObject() {
        Object actual = new Object();
        Object expected = new Object();

        assertNotSame(expected, actual);
        assertNotSame("Expected two objects to not be the same but they are",
                expected,
                actual
        );
    }
}
