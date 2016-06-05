package com.testwithspring.starter.assertions;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * This class demonstrates how we can write assertions for {@code List} objects
 * by using JUnit 4 and Hamcrest.
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
    public void sizeOfEmptyListShouldBeZero() {
        List<String> emptyList = new ArrayList<>();

        assertThat(emptyList, hasSize(0));
        assertThat(String.format("Expected size of empty list to be: 0 but was: %d",
                        emptyList.size()
                ),
                emptyList,
                hasSize(0)
        );
    }

    @Test
    public void listShouldContainCorrectObjectsInCorrectOrder() {
        assertThat(list, contains(first, second));
    }

    @Test
    public void listShouldContainCorrectObjectsInAnyOrder() {
        assertThat(list, containsInAnyOrder(first, second));
    }

    @Test
    public void listShouldNotContainIncorrectObjects() {
        Object incorrect = new Object();
        assertThat(list.contains(incorrect), is(false));
    }
}
