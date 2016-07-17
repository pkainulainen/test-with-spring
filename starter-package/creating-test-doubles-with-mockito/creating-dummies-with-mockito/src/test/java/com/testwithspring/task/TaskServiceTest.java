package com.testwithspring.task;

import com.testwithspring.user.LoggedInUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.testwithspring.task.TestDoubles.dummy;
import static org.mockito.Mockito.mock;

/**
 * This class demonstrates how we can create dummies with Mockito.
 */
public class TaskServiceTest {

    private final Long SOURCE_TASK_ID = 99L;

    private TaskService service;

    @Before
    public void configureSystemUnderTest() {
        service = new TaskService();
    }

    /**
     * This is OK. The reason why I don't like this approach is that
     * I have to use the word mock even though the created test double
     * is a dummy. This might seem like nitpicking but small things
     * add up.
     */
    @Test
    public void createDummyInTestMethod() {
        LoggedInUser notImportant = mock(LoggedInUser.class);
        service.createCopyOf(SOURCE_TASK_ID, notImportant);
    }

    /**
     * This looks pretty horrible. Even though we can clean it up
     * by using Java 8 and moving this code into our object mother class,
     * I think that we shouldn't configure the default answer of a dummy
     * because this means that our tests won't pass if we use logging.
     * Of course we could add additional code that fixes this, but it is
     * a terrible idea.
     */
    @Test
    public void createStrictDummyInTestMethod() {
        LoggedInUser notImportant = mock(LoggedInUser.class, new Answer<LoggedInUser>() {
            @Override
            public LoggedInUser answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("The system under test invoked a dummy.");
            }
        });
        service.createCopyOf(SOURCE_TASK_ID, notImportant);
    }

    /**
     * I like this method because I can use the word dummy when I
     * create my test double, but the downside that I have to
     * create an object mother class that does the trick.
     */
    @Test
    public void createDummyWithFactoryMethod() {
        LoggedInUser notImportant = dummy(LoggedInUser.class);
        service.createCopyOf(SOURCE_TASK_ID, notImportant);
    }
}
