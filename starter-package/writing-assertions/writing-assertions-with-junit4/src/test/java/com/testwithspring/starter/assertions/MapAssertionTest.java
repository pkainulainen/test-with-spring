package com.testwithspring.starter.assertions;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This class demonstrates how we can write assertions for {@code Map} objects
 * by using JUnit 4.
 *
 * @author Petri Kainulainen
 */
public class MapAssertionTest {

    private static final String INCORRECT_KEY = "incorrectKey";
    private static final String KEY = "key";

    private Object expected;
    private Map<String, Object> map;

    @Before
    public void createAndInitializeMap() {
        map = new HashMap<>();

        expected = new Object();
        map.put(KEY, expected);
    }

    @Test
    public void shouldContainObjectWithCorrectKey() {
        Object actual = map.get(KEY);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotContainObjectWithIncorrectKey() {
        Object actual = map.get(INCORRECT_KEY);
        assertNull(actual);
    }
}
