package com.testwithspring.starter.assertions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test class demonstrates how we can assertions for conditions that
 * can be either true or false.
 *
 * @author Petri Kainulainen
 */
public class BooleanAssertionTest {

    private static final boolean FALSE = false;
    private static final boolean TRUE = true;

    @Test
    public void booleanShouldBeTrue() {
        assertTrue(TRUE);
        assertTrue("isTrue should be true but was false", TRUE);
    }

    @Test
    public void booleanShouldBeFalse() {
        assertFalse(FALSE);
        assertFalse("isFalse should be false but was true", FALSE);
    }

    @Test
    public void sizeOfEmptyListShouldBeZero() {
        List<String> emptyList = new ArrayList<>();

        int actualSize = emptyList.size();
        assertTrue(actualSize == 0);
        assertTrue(String.format("Expected size of empty list to be: 0 but was: %d", actualSize), actualSize == 0);
    }
}
