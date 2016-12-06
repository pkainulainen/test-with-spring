package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.testwithspring.intermediate.task.TaskDTOAssert.assertThatTask;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class RepositoryTaskCrudServiceTest {

    private static final Long CREATOR_ID = 1L;
    private static final String DESCRIPTION = "test the method that finds tasks";
    private static final ZonedDateTime NOW = ZonedDateTime.now();
    private static final Long TASK_ID = 1L;
    private static final String TITLE = "Write an example test";

    private TaskRepository repository;
    private RepositoryTaskCrudService service;

    @Before
    public void configureSystemUnderTest() {
        repository = mock(TaskRepository.class);
        service = new RepositoryTaskCrudService(repository);
    }

    public class Create {

        private TaskFormDTO input;

        @Before
        public void configureTestCases() {
            input = createInput();
            returnPersistedTask();
        }

        private TaskFormDTO createInput() {
            TaskFormDTO task = new TaskFormDTO();

            task.setDescription(DESCRIPTION);
            task.setTitle(TITLE);

            return task;
        }

        private void returnPersistedTask() {
            given(repository.save(isA(Task.class)))
                    .willAnswer(invocation -> {
                        Task saved = (Task) invocation.getArguments()[0];
                        ReflectionTestUtils.setField(saved, "creationTime", NOW);
                        ReflectionTestUtils.setField(saved, "id", TASK_ID);
                        ReflectionTestUtils.setField(saved, "modificationTime", NOW);
                        return saved;
                    });
        }

        @Test
        public void shouldReturnTaskWithoutAssignee() {
            TaskDTO task = service.create(input);
            assertThat(task.getAssigneeId()).isNull();
        }

        @Test
        public void shouldReturnTaskWithCorrectCreationTime() {
            TaskDTO task = service.create(input);
            assertThat(task.getCreationTime()).isEqualTo(NOW);
        }

        @Test
        public void shouldReturnTaskWithCorrectCreator() {
            TaskDTO task = service.create(input);
            assertThat(task.getCreatorId()).isEqualTo(CREATOR_ID);
        }

        @Test
        public void shouldReturnTaskWithCorrectDescription() {
            TaskDTO task = service.create(input);
            assertThat(task.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        public void shouldReturnTaskWithCorrectId() {
            TaskDTO task = service.create(input);
            assertThat(task.getId()).isEqualByComparingTo(TASK_ID);
        }

        @Test
        public void shouldReturnTaskWithCorrectModificationTime() {
            TaskDTO task = service.create(input);
            assertThat(task.getModificationTime()).isEqualTo(NOW);
        }

        @Test
        public void shouldReturnOpenTask() {
            TaskDTO task = service.create(input);
            assertThatTask(task).isOpen();
        }

        /**
         * We cannot write tests which ensure that the tested method
         * creates a new task that has no creationTime, id, and
         * modificationTime because we set the field values of these
         * fields when we configure the returned object.
         *
         * That is why we have to write integration tests which ensure
         * that the created task has the correct creationTime, id, and
         * modificationTime.
         */

        @Test
        public void shouldCreateTaskWithoutAssignee() {
            service.create(input);

            verify(repository, times(1)).save(assertArg(
                    t -> assertThat(t.getAssignee()).isNull()
            ));
        }

        @Test
        public void shouldCreateTaskWithCorrectCreator() {
            service.create(input);

            verify(repository, times(1)).save(assertArg(
                    t -> assertThat(t.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID)
            ));
        }

        @Test
        public void shouldCreateTaskWithCorrectDescription() {
            service.create(input);

            verify(repository, times(1)).save(assertArg(
                    t -> assertThat(t.getDescription()).isEqualTo(DESCRIPTION)
            ));
        }

        @Test
        public void shouldCreateTaskWithCorrectTitle() {
            service.create(input);

            verify(repository, times(1)).save(assertArg(
                    t -> assertThat(t.getTitle()).isEqualTo(TITLE)
            ));
        }

        @Test
        public void shouldCreateOpenTask() {
            service.create(input);

            verify(repository, times(1)).save(assertArg(
                    t -> TaskAssert.assertThatTask(t).isOpen()
            ));
        }
    }

    public class Delete {

        public class WhenTaskIsNotFound {

            @Before
            public void returnNoTask() {
                given(repository.findOne(TASK_ID)).willReturn(Optional.empty());
            }

            @Test(expected = TaskNotFoundException.class)
            public void shouldThrowException() {
                service.delete(TASK_ID);
            }
        }

        public class WhenTaskIsFound {

            private Task found;

            @Before
            public void configureTestCases() {
                found = createOpenTask();
                returnTask(found);
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

            private void returnTask(Task found) {
                given(repository.findOne(TASK_ID)).willReturn(Optional.of(found));
            }

            @Test
            public void shouldDeleteFoundTask() {
                service.delete(TASK_ID);
                verify(repository, times(1)).delete(found);
            }

            @Test
            public void shouldReturnTaskWithoutAssignee() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getAssigneeId()).isNull();
            }

            @Test
            public void shouldReturnTaskWithCorrectCreationTime() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getCreationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectCreator() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getCreatorId()).isEqualTo(CREATOR_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectDescription() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getDescription()).isEqualTo(DESCRIPTION);
            }

            @Test
            public void shouldReturnTaskWithCorrectId() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getId()).isEqualTo(TASK_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectModificationTime() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getModificationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectTitle() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getTitle()).isEqualTo(TITLE);
            }

            @Test
            public void shouldReturnOpenTask() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThatTask(deleted).isOpen();
            }
        }
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

    public class Update {

        public class WhenTaskIsNotFound {

            @Before
            public void returnEmptyOptional() {
                given(repository.findOne(TASK_ID)).willReturn(Optional.empty());
            }

            @Test(expected = TaskNotFoundException.class)
            public void shouldThrowException() {
                TaskFormDTO input = createInput();
                service.update(input);
            }

            private TaskFormDTO createInput() {
                TaskFormDTO input = new TaskFormDTO();

                input.setId(TASK_ID);

                return input;
            }
        }

        public class WhenTaskIsFound {

            private final String NEW_DESCRIPTION = "Test the method that updates a task";
            private final String NEW_TITLE = "Write new unit test";

            private TaskFormDTO input;
            private Task updated;

            @Before
            public void configureTestCases() {
                input = createInput();
                updated = createOpenTask();
                returnTask(updated);
            }

            private TaskFormDTO createInput() {
                TaskFormDTO input = new TaskFormDTO();

                input.setId(TASK_ID);
                input.setDescription(NEW_DESCRIPTION);
                input.setTitle(NEW_TITLE);

                return input;
            }

            private Task createOpenTask() {
                return new TaskBuilder()
                        .withCreationTime(NOW)
                        .withCreator(CREATOR_ID)
                        .withDescription(DESCRIPTION)
                        .withId(TASK_ID)
                        .withModificationTime(NOW)
                        .withTitle(TITLE)
                        .withStatusOpen()
                        .build();
            }

            private void returnTask(Task task) {
                given(repository.findOne(TASK_ID)).willReturn(Optional.of(task));
            }

            @Test
            public void shouldNotSetAssignee() {
                service.update(input);
                assertThat(updated.getAssignee()).isNull();
            }

            @Test
            public void shouldNotSetCloser() {
                service.update(input);
                assertThat(updated.getCloser()).isNull();
            }

            @Test
            public void shouldNotChangeCreator() {
                service.update(input);
                assertThat(updated.getCreator().getUserId()).isEqualTo(CREATOR_ID);
            }

            @Test
            public void shouldUpdateDescription() {
                service.update(input);
                assertThat(updated.getDescription()).isEqualTo(NEW_DESCRIPTION);
            }

            @Test
            public void shouldUpdateTitle() {
                service.update(input);
                assertThat(updated.getTitle()).isEqualTo(NEW_TITLE);
            }

            @Test
            public void shouldNotChangeStatusOfTask() {
                service.update(input);
                TaskAssert.assertThatTask(updated).isOpen();
            }

            @Test
            public void shouldReturnTaskWithoutAssignee() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getAssigneeId()).isNull();
            }

            @Test
            public void shouldReturnTaskWithoutCloser() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getCloserId()).isNull();
            }

            @Test
            public void shouldReturnTaskWithCorrectCreationTime() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getCreationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectCreator() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getCreatorId()).isEqualByComparingTo(CREATOR_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectDescription() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getDescription()).isEqualTo(NEW_DESCRIPTION);
            }

            @Test
            public void shouldReturnTaskWithCorrectId() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getId()).isEqualByComparingTo(TASK_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectModificationTime() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getModificationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectTitle() {
                TaskDTO updated = service.update(input);
                assertThat(updated.getTitle()).isEqualTo(NEW_TITLE);
            }

            @Test
            public void shouldReturnOpenTask() {
                TaskDTO updated = service.update(input);
                assertThatTask(updated).isOpen();
            }
        }
    }
}
