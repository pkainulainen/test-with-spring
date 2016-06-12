package com.testwithspring.starter.assertions;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This class demonstrates how we can write assertions for {@code List} objects
 * by using AssertJ.
 *
 * @author Petri Kainulainen
 */
public class ListAssertionTest {

    private static final int EXPECTED_SIZE = 2;

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

        assertThat(emptyList)
                .overridingErrorMessage("Expected size of empty list to be: 0 but was: %d",
                        emptyList.size()
                )
                .isEmpty();
    }

    @Test
    public void sizeOfListShouldBeTwo() {
        assertThat(list)
                .overridingErrorMessage("Expected size of list to be: %d but was: %d",
                        EXPECTED_SIZE,
                        list.size()
                )
                .hasSize(EXPECTED_SIZE);
    }

    @Test
    public void listShouldContainCorrectObjectsInCorrectOrder() {
        assertThat(list).containsExactly(first, second);
    }

    @Test
    public void SizeOfListShouldBeTwoAndItShouldContainCorrectObjectsInCorrectOrder() {
        assertThat(list)
                .hasSize(EXPECTED_SIZE)
                .containsExactly(first, second);
    }

    @Test
    public void listShouldContainCorrectObjectsInAnyOrder() {
        assertThat(list).containsExactlyInAnyOrder(second, first);
    }

    @Test
    public void listShouldNotContainIncorrectObjects() {
        Object incorrect = new Object();
        assertThat(list).doesNotContain(incorrect);
    }
}
