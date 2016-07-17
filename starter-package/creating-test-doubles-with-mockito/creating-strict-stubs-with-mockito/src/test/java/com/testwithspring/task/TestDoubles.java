package com.testwithspring.task;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;

/**
 * This is an object mother class that provides convenience methods
 * that helps you to create test doubles with Mockito.
 *
 * This class two goals:
 * <ol>
 *     <li>
 *         It helps you to use the correct language when you are
 *         creating different test doubles (i.e. you are not forced
 *         to use the word 'mock')
 *     </li>
 *     <li>
 *         It moves unnecessary boilerplate code away from the test class.
 *         To be more specific, you don't have to configure the default
 *         answer in the test class because this class takes care of it.
 *     </li>
 * </ol>
 *  and
 */
public final class TestDoubles {

    /**
     * Prevents instantiation.
     */
    private TestDoubles() {}

    /**
     * Creates a dummy.
     * @param dummyClass    The class that represents the created dummy object.
     * @param <T>           The type of the created dummy.
     * @return              The created dummy object.
     */
    public static <T> T dummy(Class<T> dummyClass) {
        return mock(dummyClass);
    }

    /**
     * Creates a stub that throws a {@code RuntimeException} if the system under
     * test invokes a method whose answer has not been configured.
     *
     * @param stubClass     The class that represents the created stub.
     * @param <T>           The type of the created stub.
     * @return              The created stub.
     */
    public static <T> T strictStub(Class<T> stubClass) {
        return mock(stubClass, new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("The system under test invoked an unexpected method of a stub");
            }
        });
    }

    /**
     * Creates a stub that returns Mockito's default answer if the system under
     * test invokes a method whose answer has not been configured.
     *
     * @param stubClass     The class that represents the created stub.
     * @param <T>           The type of the created stub.
     * @return              The created stub.
     */
    public static <T> T stub(Class<T> stubClass) {
        return mock(stubClass);
    }
}
