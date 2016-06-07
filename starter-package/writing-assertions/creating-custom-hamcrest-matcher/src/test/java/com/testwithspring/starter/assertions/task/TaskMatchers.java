package com.testwithspring.starter.assertions.task;

import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.is;

/**
 * This an object mother class that demonstrates how we can create custom
 * Hamcrest matchers. This class has two different matchers that are described
 * in the following:
 * <ul>
 *     <li>
 *         Extending the abstract {@code FeatureMatcher} class is a great choice
 *         if we want to write an assertion that asserts a single value (like a property value).
 *     </li>
 *     <li>
 *         Extending the abstract {@code TypeSafeDiagnosingMatcher} class is a great choice
 *         if we want to write an assertion that assertions multiple values. Typically we
 *         should use this matcher if we want to write assertions for the states of our
 *         objects (e.g. a task is open).
 *     </li>
 * </ul>
 */
public final class TaskMatchers {

    private TaskMatchers() {}

    /**
     * Creates a Hamcrest matcher which ensures that the title of a {@code Task} object
     * is equal to the expected title.
     * @param expectedTitle The expected title
     * @return  The created Hamcrest matcher.
     */
    public static FeatureMatcher<Task, String> hasTitle(final String expectedTitle) {
        return new FeatureMatcher<Task, String>(is(expectedTitle), "title", "title") {

            @Override
            protected String featureValueOf(Task actual) {
                return actual.getTitle();
            }
        };
    }

    /**
     * Creates a Hamcrest matcher which ensures that the inspected {@code Task} object
     * is open.
     * @return  The created Hamcrest matcher.
     */
    public static TypeSafeDiagnosingMatcher<Task> isOpen() {
        return new TypeSafeDiagnosingMatcher<Task>() {

            @Override
            protected boolean matchesSafely(Task item, Description mismatchDescription) {
                mismatchDescription.appendText(" but had status: ")
                        .appendValue(item.getStatus())
                        .appendText(" and resolution: ")
                        .appendValue(item.getResolution());

                return (item.getStatus() == TaskStatus.OPEN) && (item.getResolution() == null);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("An open task should have the status: ")
                        .appendValue(TaskStatus.OPEN)
                        .appendText(" and resolution: ")
                        .appendValue(null);
            }
        };
    }
}
