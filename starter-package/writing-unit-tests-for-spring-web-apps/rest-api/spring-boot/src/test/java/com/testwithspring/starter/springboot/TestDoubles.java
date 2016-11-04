package com.testwithspring.starter.springboot;

import static org.mockito.Mockito.mock;

/**
 * Provides static factory methods that can be used for
 * creating test doubles.
 */
public final class TestDoubles {

    /**
     * Prevents instantiation.
     */
    private TestDoubles() {}

    /**
     * Creates a new dummy.
     * @param dummyClass    Identifies the type of the created dummy.
     * @param <T>
     * @return  The created dummy object.
     */
    public static <T> T dummy(Class<T> dummyClass) {
        return mock(dummyClass);
    }

    /**
     * Creates a new stub.
     * @param stubClass Identifies the type of the created stub object.
     * @param <T>
     * @return  The created stub object.
     */
    public static <T> T stub(Class<T> stubClass) {
        return mock(stubClass);
    }
}
