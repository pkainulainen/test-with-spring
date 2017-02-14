package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.ReflectionFieldUtil;
import com.testwithspring.intermediate.UnitTest;
import com.testwithspring.intermediate.common.NotFoundException;
import com.testwithspring.intermediate.user.LoggedInUser;
import com.testwithspring.intermediate.user.LoggedInUserBuilder;
import com.testwithspring.intermediate.user.PersonDTO;
import com.testwithspring.intermediate.user.PersonFinder;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.testwithspring.intermediate.TestDoubles.dummy;
import static com.testwithspring.intermediate.TestDoubles.stub;
import static com.testwithspring.intermediate.task.TaskDTOAssert.assertThatTask;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class RepositoryTaskCrudServiceTest {

    private static final Long CREATOR_ID = 1L;
    private static final String CREATOR_NAME = "John Doe";
    private static final String DESCRIPTION = "test the method that finds tasks";
    private final Long MODIFIER_ID = 456L;
    private final String MODIFIER_NAME = "Jane Doe";
    private static final ZonedDateTime NOW = ZonedDateTime.now();
    private static final Long TASK_ID = 1L;
    private static final String TITLE = "Write an example test";

    private static final Long TAG_ID = 44L;
    private static final String TAG_NAME = "testing";

    private PersonFinder personFinder;
    private TaskRepository taskRepository;
    private RepositoryTaskCrudService service;

    @Before
    public void configureSystemUnderTest() {
        personFinder = stub(PersonFinder.class);
        taskRepository = mock(TaskRepository.class);
        service = new RepositoryTaskCrudService(personFinder, taskRepository);
    }

    public class Create {

        private TaskFormDTO input;
        private LoggedInUser loggedInUser;

        @Before
        public void configureTestCases() {
            input = createInput();
            loggedInUser = createLoggedInUser();
            returnCreator();
            returnPersistedTask();
        }

        private TaskFormDTO createInput() {
            TaskFormDTO task = new TaskFormDTO();

            task.setDescription(DESCRIPTION);
            task.setTitle(TITLE);

            return task;
        }

        private LoggedInUser createLoggedInUser() {
            return new LoggedInUserBuilder()
                    .withId(CREATOR_ID)
                    .build();
        }

        private void returnPersistedTask() {
            given(taskRepository.save(isA(Task.class)))
                    .willAnswer(invocation -> {
                        Task saved = (Task) invocation.getArguments()[0];
                        ReflectionFieldUtil.setFieldValue(saved, "creationTime", NOW);
                        ReflectionFieldUtil.setFieldValue(saved, "id", TASK_ID);
                        ReflectionFieldUtil.setFieldValue(saved, "modificationTime", NOW);
                        return saved;
                    });
        }

        @Test
        public void shouldReturnTaskWithoutAssignee() {
            TaskDTO task = service.create(input, loggedInUser);
            assertThat(task.getAssignee()).isNull();
        }

        @Test
        public void shouldReturnTaskWithCorrectCreationTime() {
            TaskDTO task = service.create(input, loggedInUser);
            assertThat(task.getCreationTime()).isEqualTo(NOW);
        }

        @Test
        public void shouldReturnTaskWithCorrectCreator() {
            TaskDTO task = service.create(input, loggedInUser);

            PersonDTO creator = task.getCreator();
            assertThat(creator.getName()).isEqualTo(CREATOR_NAME);
            assertThat(creator.getUserId()).isEqualTo(CREATOR_ID);
        }

        @Test
        public void shouldReturnTaskWithCorrectModifier() {
            TaskDTO task = service.create(input, loggedInUser);

            PersonDTO modifier = task.getModifier();
            assertThat(modifier.getName()).isEqualTo(CREATOR_NAME);
            assertThat(modifier.getUserId()).isEqualTo(CREATOR_ID);
        }

        @Test
        public void shouldReturnTaskWithCorrectDescription() {
            TaskDTO task = service.create(input, loggedInUser);
            assertThat(task.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        public void shouldReturnTaskWithCorrectId() {
            TaskDTO task = service.create(input, loggedInUser);
            assertThat(task.getId()).isEqualByComparingTo(TASK_ID);
        }

        @Test
        public void shouldReturnTaskWithCorrectModificationTime() {
            TaskDTO task = service.create(input, loggedInUser);
            assertThat(task.getModificationTime()).isEqualTo(NOW);
        }

        @Test
        public void shouldReturnTaskThatHasNoTags() {
            TaskDTO task = service.create(input, loggedInUser);
            assertThat(task.getTags()).isEmpty();
        }

        @Test
        public void shouldReturnOpenTask() {
            TaskDTO task = service.create(input, loggedInUser);
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
            service.create(input, loggedInUser);

            verify(taskRepository, times(1)).save(assertArg(
                    t -> assertThat(t.getAssignee()).isNull()
            ));
        }

        @Test
        public void shouldCreateTaskWithCorrectCreator() {
            service.create(input, loggedInUser);

            verify(taskRepository, times(1)).save(assertArg(
                    t -> assertThat(t.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID)
            ));
        }

        @Test
        public void shouldCreateTaskWithCorrectModifier() {
            service.create(input, loggedInUser);

            verify(taskRepository, times(1)).save(assertArg(
                    t -> assertThat(t.getModifier().getUserId()).isEqualByComparingTo(CREATOR_ID)
            ));
        }

        @Test
        public void shouldCreateTaskWithCorrectDescription() {
            service.create(input, loggedInUser);

            verify(taskRepository, times(1)).save(assertArg(
                    t -> assertThat(t.getDescription()).isEqualTo(DESCRIPTION)
            ));
        }

        @Test
        public void shouldCreateTaskWithCorrectTitle() {
            service.create(input, loggedInUser);

            verify(taskRepository, times(1)).save(assertArg(
                    t -> assertThat(t.getTitle()).isEqualTo(TITLE)
            ));
        }

        @Test
        public void shouldCreateOpenTask() {
            service.create(input, loggedInUser);

            verify(taskRepository, times(1)).save(assertArg(
                    t -> TaskAssert.assertThatTask(t).isOpen()
            ));
        }

        @Test
        public void shouldCreateTaskThatHasNoTags() {
            service.create(input, loggedInUser);

            verify(taskRepository, times(1)).save(assertArg(
                    t -> assertThat(t.getTags()).isEmpty()
            ));
        }
    }

    public class Delete {

        public class WhenTaskIsNotFound {

            @Before
            public void returnNoTask() {
                given(taskRepository.findOne(TASK_ID)).willReturn(Optional.empty());
            }

            @Test(expected = NotFoundException.class)
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
                returnCreator();
            }

            private Task createOpenTask() {
                return new TaskBuilder()
                        .withId(TASK_ID)
                        .withCreationTime(NOW)
                        .withCreator(CREATOR_ID)
                        .withDescription(DESCRIPTION)
                        .withModificationTime(NOW)
                        .withModifier(CREATOR_ID)
                        .withTitle(TITLE)
                        .withStatusOpen()
                        .build();
            }

            private void returnTask(Task found) {
                given(taskRepository.findOne(TASK_ID)).willReturn(Optional.of(found));
            }

            @Test
            public void shouldDeleteFoundTask() {
                service.delete(TASK_ID);
                verify(taskRepository, times(1)).delete(found);
            }

            @Test
            public void shouldReturnTaskWithoutAssignee() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getAssignee()).isNull();
            }

            @Test
            public void shouldReturnTaskWithCorrectCreationTime() {
                TaskDTO deleted = service.delete(TASK_ID);
                assertThat(deleted.getCreationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectCreator() {
                TaskDTO deleted = service.delete(TASK_ID);

                PersonDTO creator = deleted.getCreator();
                assertThat(creator.getName()).isEqualTo(CREATOR_NAME);
                assertThat(creator.getUserId()).isEqualTo(CREATOR_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectModifier() {
                TaskDTO deleted = service.delete(TASK_ID);

                PersonDTO modifier = deleted.getModifier();
                assertThat(modifier.getName()).isEqualTo(CREATOR_NAME);
                assertThat(modifier.getUserId()).isEqualTo(CREATOR_ID);
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

            public class WhenFoundTaskHasNoTags {

                @Test
                public void shouldReturnTaskThatHasNoTags() {
                    TaskDTO deleted = service.delete(TASK_ID);
                    assertThat(deleted.getTags()).isEmpty();
                }
            }

            public class WhenFoundTaskHasOneTag {

                @Before
                public void returnFoundTask() {
                    Task found = createTask();
                    returnTask(found);
                }

                private Task createTask() {
                    Tag tag = new TagBuilder()
                            .withId(TAG_ID)
                            .withName(TAG_NAME)
                            .build();

                    return new TaskBuilder()
                            .withId(TASK_ID)
                            .withCreationTime(NOW)
                            .withCreator(CREATOR_ID)
                            .withDescription(DESCRIPTION)
                            .withModificationTime(NOW)
                            .withTags(tag)
                            .withTitle(TITLE)
                            .withStatusOpen()
                            .build();
                }

                @Test
                public void shouldReturnTaskThatHasOneTag() {
                    TaskDTO deleted = service.delete(TASK_ID);
                    assertThat(deleted.getTags()).hasSize(1);
                }

                @Test
                public void shouldReturnTaskThatHasCorrectTag() {
                    TaskDTO deleted = service.delete(TASK_ID);
                    TagDTO tag = deleted.getTags().get(0);

                    assertThat(tag.getId()).isEqualByComparingTo(TAG_ID);
                    assertThat(tag.getName()).isEqualTo(TAG_NAME);
                }
            }

        }
    }

    public class FindAll {

        private final TaskStatus STATUS = TaskStatus.OPEN;

        @Before
        public void returnOneTask() {
            TaskListDTO task = createTask();
            given(taskRepository.findAll()).willReturn(Collections.singletonList(task));
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
                given(taskRepository.findOne(TASK_ID)).willReturn(Optional.empty());
            }

            @Test(expected = NotFoundException.class)
            public void shouldThrowException() {
                service.findById(TASK_ID);
            }
        }

        public class WhenTaskIsFound {

            private final Long ASSIGNEE_ID = 7L;
            private final String ASSIGNEE_NAME = "Anne Assignee";

            @Before
            public void returnFoundTask() {
                Task found = createTask();
                returnTask(found);
                returnCreator();
            }

            private Task createTask() {
                return new TaskBuilder()
                        .withId(TASK_ID)
                        .withCreationTime(NOW)
                        .withCreator(CREATOR_ID)
                        .withDescription(DESCRIPTION)
                        .withModificationTime(NOW)
                        .withModifier(CREATOR_ID)
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

                PersonDTO creator = task.getCreator();
                assertThat(creator.getName()).isEqualTo(CREATOR_NAME);
                assertThat(creator.getUserId()).isEqualTo(CREATOR_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectModifier() {
                TaskDTO task = service.findById(TASK_ID);

                PersonDTO modifier = task.getModifier();
                assertThat(modifier.getName()).isEqualTo(CREATOR_NAME);
                assertThat(modifier.getUserId()).isEqualTo(CREATOR_ID);
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
                    assertThat(task.getAssignee()).isNull();
                }
            }

            public class WhenFoundTaskHasAssignee {

                @Before
                public void returnFoundTask() {
                    Task found = createTask();
                    returnTask(found);
                    returnAssignee();
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

                    PersonDTO assignee = task.getAssignee();
                    assertThat(assignee.getName()).isEqualTo(ASSIGNEE_NAME);
                    assertThat(assignee.getUserId()).isEqualByComparingTo(ASSIGNEE_ID);
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
                private final String CLOSER_NAME = "Chris Closer";

                private PersonDTO closer;

                @Before
                public void returnOpenTask() {
                    Task finishedTask = createOpenTask();
                    returnTask(finishedTask);
                    returnAssignee();
                    closer = createCloser();
                    returnCloser(closer);
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

                private PersonDTO createCloser() {
                    PersonDTO closer = new PersonDTO();
                    closer.setName(CLOSER_NAME);
                    closer.setUserId(CLOSER_ID);
                    return closer;
                }

                private void returnCloser(PersonDTO closer) {
                    given(personFinder.findPersonInformationByUserId(CLOSER_ID)).willReturn(closer);
                }

                @Test
                public void shouldReturnTaskThatWasClosedWithResolutionDone() {
                    TaskDTO task = service.findById(TASK_ID);
                    assertThatTask(task).wasClosedWithResolutionDoneBy(closer);
                }
            }

            public class WhenFoundTaskHasNoTags {

                @Test
                public void shouldReturnTaskThatHasNoTags() {
                    TaskDTO task = service.findById(TASK_ID);
                    assertThat(task.getTags()).isEmpty();
                }
            }

            public class WhenFoundTaskHasOneTag {

                @Before
                public void returnFoundTask() {
                    Task found = createTask();
                    returnTask(found);
                }

                private Task createTask() {
                    Tag tag = new TagBuilder()
                            .withId(TAG_ID)
                            .withName(TAG_NAME)
                            .build();

                    return new TaskBuilder()
                            .withId(TASK_ID)
                            .withAssignee(ASSIGNEE_ID)
                            .withCreationTime(NOW)
                            .withCreator(CREATOR_ID)
                            .withDescription(DESCRIPTION)
                            .withModificationTime(NOW)
                            .withTags(tag)
                            .withTitle(TITLE)
                            .withStatusOpen()
                            .build();
                }

                @Test
                public void shouldReturnTaskThatHasOneTag() {
                    TaskDTO task = service.findById(TASK_ID);
                    assertThat(task.getTags()).hasSize(1);
                }

                @Test
                public void shouldReturnTaskThatHasCorrectTag() {
                    TaskDTO task = service.findById(TASK_ID);
                    TagDTO tag = task.getTags().get(0);

                    assertThat(tag.getId()).isEqualByComparingTo(TAG_ID);
                    assertThat(tag.getName()).isEqualTo(TAG_NAME);
                }
            }

            private void returnTask(Task found) {
                given(taskRepository.findOne(TASK_ID)).willReturn(Optional.of(found));
            }

            private void returnAssignee() {
                PersonDTO assignee = new PersonDTO();
                assignee.setName(ASSIGNEE_NAME);
                assignee.setUserId(ASSIGNEE_ID);

                given(personFinder.findPersonInformationByUserId(ASSIGNEE_ID)).willReturn(assignee);
            }
        }
    }

    public class Update {

        public class WhenTaskIsNotFound {

            @Before
            public void returnEmptyOptional() {
                given(taskRepository.findOne(TASK_ID)).willReturn(Optional.empty());
            }

            @Test(expected = NotFoundException.class)
            public void shouldThrowException() {
                TaskFormDTO input = createInput();
                service.update(input, dummy(LoggedInUser.class));
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
            private LoggedInUser loggedInUser;
            private Task updated;

            @Before
            public void configureTestCases() {
                input = createInput();

                loggedInUser = new LoggedInUserBuilder()
                        .withId(MODIFIER_ID)
                        .build();

                updated = createOpenTask();
                returnTask(updated);
                returnCreator();
                returnModifier();
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
                given(taskRepository.findOne(TASK_ID)).willReturn(Optional.of(task));
            }

            @Test
            public void shouldNotSetAssignee() {
                service.update(input, loggedInUser);
                assertThat(updated.getAssignee()).isNull();
            }

            @Test
            public void shouldNotSetCloser() {
                service.update(input, loggedInUser);
                assertThat(updated.getCloser()).isNull();
            }

            @Test
            public void shouldNotChangeCreator() {
                service.update(input, loggedInUser);
                assertThat(updated.getCreator().getUserId()).isEqualByComparingTo(CREATOR_ID);
            }

            @Test
            public void shouldUpdateModifier() {
                service.update(input, loggedInUser);
                assertThat(updated.getModifier().getUserId()).isEqualByComparingTo(MODIFIER_ID);
            }

            @Test
            public void shouldUpdateDescription() {
                service.update(input, loggedInUser);
                assertThat(updated.getDescription()).isEqualTo(NEW_DESCRIPTION);
            }

            @Test
            public void shouldUpdateTitle() {
                service.update(input, loggedInUser);
                assertThat(updated.getTitle()).isEqualTo(NEW_TITLE);
            }

            @Test
            public void shouldNotChangeStatusOfTask() {
                service.update(input, loggedInUser);
                TaskAssert.assertThatTask(updated).isOpen();
            }

            @Test
            public void shouldReturnTaskWithoutAssignee() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThat(updated.getAssignee()).isNull();
            }

            @Test
            public void shouldReturnTaskWithoutCloser() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThat(updated.getCloser()).isNull();
            }

            @Test
            public void shouldReturnTaskWithCorrectCreationTime() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThat(updated.getCreationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectCreator() {
                TaskDTO updated = service.update(input, loggedInUser);

                PersonDTO creator = updated.getCreator();
                assertThat(creator.getName()).isEqualTo(CREATOR_NAME);
                assertThat(creator.getUserId()).isEqualTo(CREATOR_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectModifier() {
                TaskDTO updated = service.update(input, loggedInUser);

                PersonDTO modifier = updated.getModifier();
                assertThat(modifier.getName()).isEqualTo(MODIFIER_NAME);
                assertThat(modifier.getUserId()).isEqualTo(MODIFIER_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectDescription() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThat(updated.getDescription()).isEqualTo(NEW_DESCRIPTION);
            }

            @Test
            public void shouldReturnTaskWithCorrectId() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThat(updated.getId()).isEqualByComparingTo(TASK_ID);
            }

            @Test
            public void shouldReturnTaskWithCorrectModificationTime() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThat(updated.getModificationTime()).isEqualTo(NOW);
            }

            @Test
            public void shouldReturnTaskWithCorrectTitle() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThat(updated.getTitle()).isEqualTo(NEW_TITLE);
            }

            @Test
            public void shouldReturnOpenTask() {
                TaskDTO updated = service.update(input, loggedInUser);
                assertThatTask(updated).isOpen();
            }

            public class WhenFoundTaskHasNoTags {

                @Test
                public void shouldReturnTaskThatHasNoTags() {
                    TaskDTO updated = service.update(input, loggedInUser);
                    assertThat(updated.getTags()).isEmpty();
                }
            }

            public class WhenFoundTaskHasOneTag {

                @Before
                public void returnFoundTask() {
                    Task found = createTask();
                    returnTask(found);
                }

                private Task createTask() {
                    Tag tag = new TagBuilder()
                            .withId(TAG_ID)
                            .withName(TAG_NAME)
                            .build();

                    return new TaskBuilder()
                            .withId(TASK_ID)
                            .withCreationTime(NOW)
                            .withCreator(CREATOR_ID)
                            .withDescription(DESCRIPTION)
                            .withModificationTime(NOW)
                            .withTags(tag)
                            .withTitle(TITLE)
                            .withStatusOpen()
                            .build();
                }

                @Test
                public void shouldReturnTaskThatHasOneTag() {
                    TaskDTO updated = service.update(input, loggedInUser);
                    assertThat(updated.getTags()).hasSize(1);
                }

                @Test
                public void shouldReturnTaskThatHasCorrectTag() {
                    TaskDTO updated = service.update(input, loggedInUser);
                    TagDTO tag = updated.getTags().get(0);

                    assertThat(tag.getId()).isEqualByComparingTo(TAG_ID);
                    assertThat(tag.getName()).isEqualTo(TAG_NAME);
                }
            }

        }
    }

    private void returnCreator() {
        PersonDTO creator = new PersonDTO();
        creator.setName(CREATOR_NAME);
        creator.setUserId(CREATOR_ID);

        given(personFinder.findPersonInformationByUserId(CREATOR_ID)).willReturn(creator);
    }

    private void returnModifier() {
        PersonDTO modifier = new PersonDTO();
        modifier.setName(MODIFIER_NAME);
        modifier.setUserId(MODIFIER_ID);

        given(personFinder.findPersonInformationByUserId(MODIFIER_ID)).willReturn(modifier);
    }
}
