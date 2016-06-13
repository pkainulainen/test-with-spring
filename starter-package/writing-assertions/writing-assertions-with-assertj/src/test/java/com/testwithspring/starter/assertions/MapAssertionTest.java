package com.testwithspring.starter.assertions;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class demonstrates how we can write assertions for {@code Map} objects
 * by using AssertJ.
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
        assertThat(map).containsKey(KEY);
    }

    @Test
    public void shouldNotContainIncorrectKey() {
        assertThat(map).doesNotContainKey(INCORRECT_KEY);
    }

    @Test
    public void shouldContainObjectWithCorrectKey() {
        assertThat(map).containsEntry(KEY, expected);
    }

    @Test
    public void shouldNotContainObjectWithIncorrectKey() {
        assertThat(map).doesNotContainEntry(INCORRECT_KEY, expected);
    }
}
