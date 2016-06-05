package com.testwithspring.starter.assertions;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * This class demonstrates how we can write assertions for {@code Map} objects
 * by using JUnit 4 and Hamcrest.
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
    public void shouldContainCorrectKey() {
        assertThat(map, hasKey(KEY));
    }

    @Test
    public void shouldNotContainIncorrectKey() {
        assertThat(map.containsKey(INCORRECT_KEY), is(false));
    }

    @Test
    public void shouldContainObjectWithCorrectKey() {
        assertThat(map, hasEntry(KEY, expected));
    }

    @Test
    public void shouldNotContainObjectWithIncorrectKey() {
        Object actual = map.get(INCORRECT_KEY);
        assertThat(actual, nullValue());
    }
}
