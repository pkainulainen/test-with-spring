package com.testwithspring.starter.assertions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * This test class demonstrates how we can write assertions for "simple" objects by using
 * JUnit 4.
 *
 * @author Petri Kainulainen
 */
public class ObjectAssertionTest {

    @Test
    public void nullShouldBeNull() {
        Object actual = null;

        assertNull(actual);
        assertNull(String.format("Expected actual to be null but was: %s", actual), actual);
    }

    @Test
    public void objectShouldNotBeNull() {
        Object actual = new Object();

        assertNotNull(actual);
        assertNotNull("Expected actual to be not null but was null", actual);
    }

    @Test
    public void twoIntegersShouldBeEqual() {
        Integer actual = 9;
        Integer expected = 9;

        assertEquals(expected, actual);
        assertEquals(String.format("Expected actual to be: %d but was: %d", expected, actual),
                expected,
                actual
        );
    }

    @Test
    public void twoIntegersShouldNotBeEqual() {
        Integer actual = 9;
        Integer expected = 4;

        assertNotEquals(expected, actual);
        assertNotEquals(String.format("Expected actual to not be: %d but was: %d", expected, expected),
                expected,
                actual
        );
    }

    @Test
    public void twoStringsShouldBeEqual() {
        String actual = "foo";
        String expected = "foo";

        assertEquals(expected, actual);
        assertEquals(String.format("Expected actual to be: %s but was: %s", expected, actual), expected, actual);
    }

    @Test
    public void twoStringsShouldNotBeEqual() {
        String actual = "foo";
        String expected = "fo1o";

        assertNotEquals(expected, actual);
        assertNotEquals(String.format("Expected actual to not be: %s but was: %s", expected, expected), expected, actual);
    }

    @Test
    public void shouldPointToSameObject() {
        Object actual = new Object();
        Object expected = actual;

        assertSame(expected, actual);
        assertSame("Expected two objects to point to the same object but they point to different objects",
                expected,
                actual
        );
    }

    @Test
    public void shouldNotPointToSameObject() {
        Object actual = new Object();
        Object expected = new Object();

        assertNotSame(expected, actual);
        assertNotSame("Expected two objects to not point to the same object but they point to the same object",
                expected,
                actual
        );
    }
}
