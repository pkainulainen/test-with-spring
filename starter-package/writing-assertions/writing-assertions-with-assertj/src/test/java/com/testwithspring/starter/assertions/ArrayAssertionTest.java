package com.testwithspring.starter.assertions;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class demonstrates how we can write assertions for arrays with AssertJ.
 * @author Petri Kainulainen
 */
public class ArrayAssertionTest {

    @Test
    public void twoIntArraysShouldBeEqual() {
        int[] actual = new int[]{2, 5, 7};
        int[] expected = new int[]{2, 5, 7};

        assertThat(actual)
                .overridingErrorMessage("Expected array: %s but got array: %s",
                        Arrays.toString(expected),
                        Arrays.toString(actual)
                )
                .isEqualTo(expected);
    }

    @Test
    public void twoStringArraysShouldBeEqual() {
        String[] actual = new String[] {"foo", "bar"};
        String[] expected = new String[] {"foo", "bar"};

        assertThat(actual)
                .overridingErrorMessage("Expected array: %s but got array: %s",
                        Arrays.toString(expected),
                        Arrays.toString(actual)
                )
                .isEqualTo(expected);
    }
}
