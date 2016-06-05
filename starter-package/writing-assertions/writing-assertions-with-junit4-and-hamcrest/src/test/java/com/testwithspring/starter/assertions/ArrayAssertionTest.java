package com.testwithspring.starter.assertions;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * This test class demonstrates how we can write assertions for arrays with JUnit 4 and Hamcrest.
 * @author Petri Kainulainen
 */
public class ArrayAssertionTest {

    @Test
    public void twoIntArraysShouldBeEqual() {
        int[] actual = new int[]{2, 5, 7};
        int[] expected = new int[]{2, 5, 7};

        assertThat(actual, is(expected));
        assertThat(String.format("Expected array: %s but got array: %s",
                    Arrays.toString(expected),
                    Arrays.toString(actual)
                ),
                actual,
                is(expected)
        );
    }

    @Test
    public void twoStringArraysShouldBeEqual() {
        String[] actual = new String[] {"foo", "bar"};
        String[] expected = new String[] {"foo", "bar"};

        assertThat(actual, is(expected));
        assertThat(String.format("Expected array: %s but got array: %s",
                    Arrays.toString(expected),
                    Arrays.toString(actual)
                ),
                actual,
                is(expected)
        );
    }
}
