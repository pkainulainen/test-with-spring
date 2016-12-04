package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.testwithspring.intermediate.TestDoubles.stub;
import static com.testwithspring.intermediate.task.TaskDTOAssert.assertThatTask;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class RepositoryTaskCrudServiceTest {

    private static final Long TASK_ID = 1L;
    private static final String TITLE = "Write an example test";

    private TaskRepository repository;
    private RepositoryTaskCrudService service;

    @Before
    public void configureSystemUnderTest() {
        repository = stub(TaskRepository.class);
        service = new RepositoryTaskCrudService(repository);
    }

    public class FindAll {

        private final TaskStatus STATUS = TaskStatus.OPEN;

        @Before
        public void returnOneTask() {
            TaskListDTO task = createTask();
            given(repository.findAll()).willReturn(Collections.singletonList(task));
        }

        private TaskListDTO createTask() {
            TaskListDTO task = new TaskListDTO();

            task.setId(TASK_ID);
            task.setStatus(STATUS);
            task.setTitle(TITLE);

            return task;
        }

        @Test
        public void shouldReturnOneTask() {
            List<TaskListDTO> tasks = service.findAll();
            assertThat(tasks).hasSize(1);
        }

        @Test
        public void shouldReturnTaskWithCorrectInformation() {
            TaskListDTO task = service.findAll().get(0);

            assertThat(task.getId()).isEqualByComparingTo(TASK_ID);
            assertThat(task.getStatus()).isEqualTo(STATUS);
            assertThat(task.getTitle()).isEqualTo(TITLE);
        }
    }

    public class FindById {

        public class WhenTaskIsNotFound {

            @Before
            public void returnEmptyOptional() {
                given(repository.findOne(TASK_ID)).willReturn(Optional.empty());
            }

            @Test(expected = TaskNotFoundException.class)
            public void shouldThrowException() {
                service.findById(TASK_ID);
            }
        }

        public class WhenTaskIsFound {

            private final Long ASSIGNEE_ID = 7L;
            private final Long CREATOR_ID = 5L;
            private final String DESCRIPTION = "test the method that finds tasks";
            private final ZonedDateTime NOW = ZonedDateTime.now();

            @Before
            public void returnFoundTask() {
                Task found = createTask();
                returnTask(found);
            }

            private Task createTask() {
                return new TaskBuilder()
                        .withId(TASK_ID)
                        .withCreationTime(NOW)
                        .withCreator(CREATOR_ID)
                        .withDescription(DESCRIPTION)
                        .withModificationTime(NOW)
                        .withTitle(TITLE)
                        .withStatusOpen()
                        .build();
            }

            @Test
            public void shouldReturnTaskWithCorrectId() {
                TaskDTO task = service.findById(TASK_ID);
                assertThat(task.getId()).isEqualTo(TASK_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectCreationTime() {
                TaskDTO task = service.findById(TASK_ID);
                assertThat(task.getCreationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectCreator() {
                TaskDTO task = service.findById(TASK_ID);
                assertThat(task.getCreatorId()).isEqualTo(CREATOR_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectDescription() {
                TaskDTO task = service.findById(TASK_ID);
                assertThat(task.getDescription()).isEqualTo(DESCRIPTION);
            }

            @Test
            public void shouldReturnTaskWithCorrectModificationTime() {
                TaskDTO task = service.findById(TASK_ID);
                assertThat(task.getModificationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectTitle() {
                TaskDTO task = service.findById(TASK_ID);
                assertThat(task.getTitle()).isEqualTo(TITLE);
            }

            public class WhenFoundTaskHasNoAssignee {

                @Test
                public void shouldReturnTaskWithoutAssignee() {
                    TaskDTO task = service.findById(TASK_ID);
                    assertThat(task.getAssigneeId()).isNull();
                }
            }

            public class WhenFoundTaskHasAssignee {

                @Before
                public void returnFoundTask() {
                    Task found = createTask();
                    returnTask(found);
                }

                private Task createTask() {
                    return new TaskBuilder()
                            .withId(TASK_ID)
                            .withAssignee(ASSIGNEE_ID)
                            .withCreationTime(NOW)
                            .withCreator(CREATOR_ID)
                            .withDescription(DESCRIPTION)
                            .withModificationTime(NOW)
                            .withTitle(TITLE)
                            .withStatusOpen()
                            .build();
                }

                @Test
                public void shouldReturnTaskWithCorrectAssignee() {
                    TaskDTO task = service.findById(TASK_ID);
                    assertThat(task.getAssigneeId()).isEqualTo(ASSIGNEE_ID);
                }
            }

            public class WhenOpenTaskIsFound {

                @Before
                public void returnOpenTask() {
                    Task openTask = createOpenTask();
                    returnTask(openTask);
                }

                private Task createOpenTask() {
                    return new TaskBuilder()
                            .withId(TASK_ID)
                            .withCreationTime(NOW)
                            .withCreator(CREATOR_ID)
                            .withDescription(DESCRIPTION)
                            .withModificationTime(NOW)
                            .withTitle(TITLE)
                            .withStatusOpen()
                            .build();
                }

                @Test
                public void shouldReturnOpenTask() {
                    TaskDTO task = service.findById(TASK_ID);
                    assertThatTask(task).isOpen();
                }
            }

            public class WhenFinishedTaskIsFound {

                private final Long CLOSER_ID = 9L;

                @Before
                public void returnOpenTask() {
                    Task finishedTask = createOpenTask();
                    returnTask(finishedTask);
                }

                private Task createOpenTask() {
                    return new TaskBuilder()
                            .withId(TASK_ID)
                            .withAssignee(ASSIGNEE_ID)
                            .withCreationTime(NOW)
                            .withCreator(CREATOR_ID)
                            .withDescription(DESCRIPTION)
                            .withModificationTime(NOW)
                            .withTitle(TITLE)
                            .withResolutionDone(CLOSER_ID)
                            .build();
                }

                @Test
                public void shouldReturnTaskThatWasClosedWithResolutionDone() {
                    TaskDTO task = service.findById(TASK_ID);
                    assertThatTask(task).wasClosedWithResolutionDoneBy(CLOSER_ID);
                }
            }

            private void returnTask(Task found) {
                given(repository.findOne(TASK_ID)).willReturn(Optional.of(found));
            }
        }
    }
}
