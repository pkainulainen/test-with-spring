package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.TestStringUtil;
import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static com.testwithspring.intermediate.task.TaskAssert.assertThatTask;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class TaskTest {

    public class Build {

        private final Long CREATOR_ID = 3L;
        private final String DESCRIPTION = "This example demonstrates integration testing of Spring web applications";
        private final String TITLE = "Write example application";

        public class WhenInformationIsNotValid {

            public class WhenCreatorIsNull {

                @Test(expected = NullPointerException.class)
                public void shouldThrowException() {
                    Task.getBuilder()
                            .withCreator(null)
                            .withDescription(DESCRIPTION)
                            .withTitle(TITLE)
                            .build();
                }
            }

            public class WhenDescriptionIsTooLong {

                @Test(expected = IllegalStateException.class)
                public void shouldThrowException() {
                    String tooLongDescription = TestStringUtil.createStringWithLength(501);
                    Task.getBuilder()
                            .withCreator(CREATOR_ID)
                            .withDescription(tooLongDescription)
                            .withTitle(TITLE)
                            .build();
                }
            }

            public class WhenTitleIsNotValid {

                public class WhenTitleIsNull {

                    @Test(expected = NullPointerException.class)
                    public void shouldThrowException() {
                        Task.getBuilder()
                                .withCreator(CREATOR_ID)
                                .withDescription(DESCRIPTION)
                                .withTitle(null)
                                .build();
                    }
                }

                public class WhenTitleIsEmpty {

                    @Test(expected = IllegalStateException.class)
                    public void shouldThrowException() {
                        Task.getBuilder()
                                .withCreator(CREATOR_ID)
                                .withDescription(DESCRIPTION)
                                .withTitle("")
                                .build();
                    }
                }

                public class WhenTitleIsTooLong {

                    @Test(expected = IllegalStateException.class)
                    public void shouldThrowException() {
                        String tooLongTitle = TestStringUtil.createStringWithLength(101);
                        Task.getBuilder()
                                .withCreator(CREATOR_ID)
                                .withDescription(DESCRIPTION)
                                .withTitle(tooLongTitle)
                                .build();
                    }
                }
            }
        }

        public class WhenInformationIsValid {

            private String expectedDescription;
            private String expectedTitle;

            private Task task;

            @Before
            public void createTask() {
                expectedDescription = TestStringUtil.createStringWithLength(500);
                expectedTitle = TestStringUtil.createStringWithLength(100);

                task = Task.getBuilder()
                        .withCreator(CREATOR_ID)
                        .withDescription(expectedDescription)
                        .withTitle(expectedTitle)
                        .build();
            }

            @Test
            public void shouldCreateTaskWithoutAssignee() {
                assertThat(task.getAssignee()).isNull();
            }

            @Test
            public void shouldCreateTaskWithoutCreationTime() {
                assertThat(task.getCreationTime()).isNull();
            }

            @Test
            public void shouldCreateTaskWithCorrectCreator() {
                assertThat(task.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID);
            }

            @Test
            public void shouldCreateTaskWithCorrectDescription() {
                assertThat(task.getDescription()).isEqualTo(expectedDescription);
            }

            @Test
            public void shouldCreateTaskWithoutId() {
                assertThat(task.getId()).isNull();
            }

            @Test
            public void shouldCreateTaskWithoutModificationTime() {
                assertThat(task.getModificationTime()).isNull();
            }

            @Test
            public void shouldCreateTaskWithCorrectModifier() {
                assertThat(task.getModifier().getUserId()).isEqualByComparingTo(CREATOR_ID);
            }

            @Test
            public void shouldCreateOpenTask() {
                assertThatTask(task).isOpen();
            }

            @Test
            public void shouldCreateTaskWithCorrectTitle() {
                assertThat(task.getTitle()).isEqualTo(expectedTitle);
            }

            @Test
            public void shouldCreateTaskWithoutVersion() {
                assertThat(task.getVersion()).isNull();
            }
        }
    }
}
