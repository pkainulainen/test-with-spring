package com.testwithspring.starter.assertions;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class demonstrates how we can write assertions for {@code List} objects
 * by using JUnit 4.
 *
 * @author Petri Kainulainen
 */
public class ListAssertionTest {

    private Object first;
    private Object second;

    private List<Object> list;

    @Before
    public void createAndInitializeList() {
        first = new Object();
        second = new Object();

        list = Arrays.asList(first, second);
    }

    @Test
    public void listShouldHaveTwoObjects() {
        assertEquals(2, list.size());
    }

    @Test
    public void listShouldContainTheCorrectObjects() {
        assertTrue(list.contains(first));
        assertTrue(list.contains(second));
    }

    @Test
    public void listShouldNotContainIncorrectObject() {
        Object incorrect = new Object();
        assertFalse(list.contains(incorrect));
    }
}
