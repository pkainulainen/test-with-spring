package com.testwithspring.starter.assertions;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

/**
 * This test class demonstrates how we can assertions for arrays with JUnit 4.
 * @author Petri Kainulainen
 */
public class ArrayAssertionTest {

    @Test
    public void twoIntArraysShouldBeEqual() {
        int[] actual = new int[]{2, 5, 7};
        int[] expected = new int[]{2, 5, 7};

        assertArrayEquals(expected, actual);
        assertArrayEquals(
                String.format("Expected array: %s to be equal to array: %s but arrays are not equal",
                    Arrays.toString(expected),
                    Arrays.toString(actual)
                ),
                expected,
                actual
        );
    }

    @Test
    public void twoStringArraysShouldBeEqual() {
        String[] actual = new String[] {"foo", "bar"};
        String[] expected = new String[] {"foo", "bar"};

        assertArrayEquals(expected, actual);
        assertArrayEquals(
                String.format("Expected array: %s to be equal to array: %s but arrays are not equal",
                        Arrays.toString(expected),
                        Arrays.toString(actual)
                ),
                expected,
                actual
        );
    }
}
