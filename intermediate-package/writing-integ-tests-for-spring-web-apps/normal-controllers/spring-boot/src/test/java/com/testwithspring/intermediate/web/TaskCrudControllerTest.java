package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.TestStringUtil;
import com.testwithspring.intermediate.UnitTest;
import com.testwithspring.intermediate.common.NotFoundException;
import com.testwithspring.intermediate.task.*;
import com.testwithspring.intermediate.user.LoggedInUser;
import com.testwithspring.intermediate.user.PersonDTO;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static com.testwithspring.intermediate.web.WebTestConfig.*;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class TaskCrudControllerTest {

    //Validation
    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    private static final String VALIDATION_ERROR_CODE_LONG_FIELD_VALUE = "Size";

    //Task
    private static final Long ASSIGNEE_ID = 44L;
    private static final String ASSIGNEE_NAME = "Anne Assignee";
    private static final Long CREATOR_ID = 99L;
    private static final String CREATOR_NAME = "John Doe";
    private static final Long MODIFIER_ID = 33L;
    private static final String MODIFIER_NAME = "Jane Doe";
    private static final String TASK_DESCRIPTION = "description";
    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "title";

    private TaskCrudService crudService;
    private StaticMessageSource messageSource;
    private MockMvc mockMvc;

    /**
     * You need to configure all these custom components because they are
     * required if you want to write unit tests for the {@code TaskCrudController}
     * class.
     */
    @Before
    public void configureSystemUnderTest() {
        crudService = mock(TaskCrudService.class);
        messageSource = new StaticMessageSource();

        mockMvc = MockMvcBuilders.standaloneSetup(new TaskCrudController(crudService, messageSource))
                .setHandlerExceptionResolvers(exceptionResolver())
                .setLocaleResolver(fixedLocaleResolver())
                .setViewResolvers(viewResolver())
                .build();
    }

    public class DeleteTask {

        public class WhenDeletedTaskIsNotFound {

            @Before
            public void throwTaskNotFoundException() {
                given(crudService.delete(TASK_ID)).willThrow(new NotFoundException(""));
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(status().isNotFound());
            }

            @Test
            public void shouldRenderNotFoundView() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND));
            }
        }

        public class WhenDeletedTaskIsFound {

            private final String FEEDBACK_MESSAGE_KEY_TASK_DELETED = "feedback.message.task.deleted";
            private final String FEEDBACK_MESSAGE_TASK_DELETED = "Task deleted";

            /**
             * You should consider dividing the configuration of the test cases into multiple private
             * methods if the configuration has multiple different steps.
             */
            @Before
            public void configureTestCases() {
                returnDeletedTask();
                returnFeedbackMessage();
            }

            private void returnDeletedTask() {
                TaskDTO deleted = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withTitle(TASK_TITLE)
                        .build();
                given(crudService.delete(TASK_ID)).willReturn(deleted);
            }

            private void returnFeedbackMessage() {
                messageSource.addMessage(FEEDBACK_MESSAGE_KEY_TASK_DELETED,
                        WebTestConfig.LOCALE,
                        FEEDBACK_MESSAGE_TASK_DELETED
                );
            }

            @Test
            public void shouldReturnHttpResponseStatusFound() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(status().isFound());
            }

            @Test
            public void shouldRedirectUserToViewTaskListView() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK_LIST));
            }

            @Test
            public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                                FEEDBACK_MESSAGE_TASK_DELETED
                        ));
            }

            @Test
            public void shouldDeleteTaskWithCorrectId() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID));

                verify(crudService, times(1)).delete(TASK_ID);
            }
        }
    }

    public class ShowCreateTaskForm {

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/task/create"))
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderCreateTaskView() throws Exception {
            mockMvc.perform(get("/task/create"))
                    .andExpect(view().name(WebTestConstants.View.CREATE_TASK));
        }

        @Test
        public void shouldCreateAnEmptyFormObject() throws Exception {
            mockMvc.perform(get("/task/create"))
                    .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                            hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, nullValue()),
                            hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue()),
                            hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, nullValue())
                    )));
        }
    }

    public class ProcessCreateTaskForm {

        /**
         * Remember that you cannot test all possible combinations because it would
         * take too long. You should pick the combinations that are easy to test
         * and test them.
         */
        public class WhenValidationFails {

            public class WhenEmptyFormIsSubmitted {

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderCreateTaskView() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(view().name(WebTestConstants.View.CREATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForEmptyTitle() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                                    WebTestConstants.ModelAttributeProperty.Task.TITLE,
                                    is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
                            ));
                }

                @Test
                public void shouldShowEmptyTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is("")),
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(""))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
                            ));
                }

                @Test
                public void shouldNotCreateNewTask() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    );

                    verify(crudService, never()).create(isA(TaskFormDTO.class), isA(LoggedInUser.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongDescription {

                private String maxLengthTitle;
                private String tooLongDescription;

                @Before
                public void createTitleAndDescription() {
                    maxLengthTitle = TestStringUtil.createStringWithLength(MAX_LENGTH_TITLE);
                    tooLongDescription = TestStringUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderCreateTaskView() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(WebTestConstants.View.CREATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                                    WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(tooLongDescription)),
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(maxLengthTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
                            ));
                }

                @Test
                public void shouldNotCreateNewTask() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    );

                    verify(crudService, never()).create(isA(TaskFormDTO.class), isA(LoggedInUser.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongTitle {

                private String tooLongTitle;
                private String maxLengthDescription;

                @Before
                public void createTitleAndDescription() {
                    tooLongTitle = TestStringUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                    maxLengthDescription = TestStringUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderCreateTaskView() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(view().name(WebTestConstants.View.CREATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongTitle() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                                    WebTestConstants.ModelAttributeProperty.Task.TITLE,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(maxLengthDescription)),
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(tooLongTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, nullValue())
                            ));
                }

                @Test
                public void shouldNotCreateNewTask() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    );

                    verify(crudService, never()).create(isA(TaskFormDTO.class), isA(LoggedInUser.class));
                }
            }
        }

        public class WhenValidationIsSuccessful {

            private final String FEEDBACK_MESSAGE_KEY_TASK_CREATED = "feedback.message.task.created";
            private final String FEEDBACK_MESSAGE_TASK_CREATED = "Task created";

            private String maxLengthDescription;
            private String maxLengthTitle;

            /**
             * You should consider dividing the configuration of the test cases into multiple private
             * methods if the configuration has multiple different steps.
             */
            @Before
            public void configureTestCases() {
                createTitleAndDescription();
                returnCreatedTask();
                returnFeedbackMessage();
            }

            private void createTitleAndDescription() {
                maxLengthDescription = TestStringUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                maxLengthTitle = TestStringUtil.createStringWithLength(MAX_LENGTH_TITLE);
            }

            private void returnCreatedTask() {
                PersonDTO creator = new PersonDTO();
                creator.setName(CREATOR_NAME);
                creator.setUserId(CREATOR_ID);

                TaskDTO created = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(creator)
                        .withTitle(maxLengthTitle)
                        .withDescription(maxLengthDescription)
                        .withStatusOpen()
                        .build();
                given(crudService.create(isA(TaskFormDTO.class), isA(LoggedInUser.class))).willReturn(created);
            }

            private void returnFeedbackMessage() {
                messageSource.addMessage(FEEDBACK_MESSAGE_KEY_TASK_CREATED,
                        WebTestConfig.LOCALE,
                        FEEDBACK_MESSAGE_TASK_CREATED
                );
            }

            @Test
            public void shouldReturnHttpStatusCodeFound() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                        .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                )
                        .andExpect(status().isFound());
            }

            @Test
            public void shouldRedirectUserToViewTaskView() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                        .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                )
                        .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is(TASK_ID.toString())));
            }

            @Test
            public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                        .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                )
                        .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                                FEEDBACK_MESSAGE_TASK_CREATED
                        ));
            }

            @Test
            public void shouldCreateNewTaskWithCorrectDescription() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                        .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getDescription()).isEqualTo(maxLengthDescription)
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldCreateNewTaskWithNullId() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                        .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getId()).isNull()
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldCreateNewTaskWithCorrectTitle() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                        .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                        ),
                        isA(LoggedInUser.class)
                );
            }
        }
    }

    public class ShowTask {

        public class WhenTaskIsNotFound {

            @Before
            public void throwTaskNotFoundException() {
                given(crudService.findById(TASK_ID)).willThrow(new NotFoundException(""));
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(status().isNotFound());
            }

            @Test
            public void shouldRenderNotFoundView() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND));
            }
        }

        public class WhenClosedTaskIsFound {

            private final Long CLOSER_ID = 931L;
            private final String CLOSER_NAME = "Chris Closer";
            private final Long TAG_ID = 33L;
            private final String TAG_NAME = "testing";

            private TaskDTO found;

            @Before
            public void returnFoundTaskWithOneTag() {
                PersonDTO assignee = new PersonDTO();
                assignee.setName(ASSIGNEE_NAME);
                assignee.setUserId(ASSIGNEE_ID);

                PersonDTO closer = new PersonDTO();
                closer.setName(CLOSER_NAME);
                closer.setUserId(CLOSER_ID);

                PersonDTO creator = new PersonDTO();
                creator.setName(CREATOR_NAME);
                creator.setUserId(CREATOR_ID);

                PersonDTO modifier = new PersonDTO();
                modifier.setName(MODIFIER_NAME);
                modifier.setUserId(MODIFIER_ID);

                TagDTO tag = new TagDTO();
                tag.setId(TAG_ID);
                tag.setName(TAG_NAME);

                found = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withAssignee(assignee)
                        .withCreator(creator)
                        .withModifier(modifier)
                        .withTags(tag)
                        .withTitle(TASK_TITLE)
                        .withDescription(TASK_DESCRIPTION)
                        .withResolutionDone(closer)
                        .build();

                given(crudService.findById(TASK_ID)).willReturn(found);
            }

            @Test
            public void shouldReturnHttpStatusCodeOk() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(status().isOk());
            }

            @Test
            public void shouldRenderViewTaskView() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(view().name(WebTestConstants.View.VIEW_TASK));
            }

            @Test
            public void shouldShowFoundTask() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ASSIGNEE, allOf(
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(ASSIGNEE_NAME)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(ASSIGNEE_ID))
                                )),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CLOSER, allOf(
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(CLOSER_NAME)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(CLOSER_ID))
                                )),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.CREATOR, allOf(
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(CREATOR_NAME)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(CREATOR_ID))
                                )),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.MODIFIER, allOf(
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.NAME, is(MODIFIER_NAME)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.Person.USER_ID, is(MODIFIER_ID))
                                )),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(TASK_TITLE)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.CLOSED)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.RESOLUTION, is(TaskResolution.DONE))
                        )));
            }

            @Test
            public void shouldShowOneTagOfFoundTask() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, hasSize(1))
                        )));
            }

            @Test
            public void shouldShowFoundTag() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TAGS, contains(
                                        allOf(
                                                hasProperty(WebTestConstants.ModelAttributeProperty.Tag.ID, is(TAG_ID)),
                                                hasProperty(WebTestConstants.ModelAttributeProperty.Tag.NAME, is(TAG_NAME))
                                        )
                                ))
                        )));
            }
        }
    }

    public class ShowTaskList {

        /**
         * These two test methods are added into this test class because both of them
         * test behavior that should happen in every case (tasks are not found and
         * tasks are not found).
         * <p>
         * You can, of course, move these test methods into the lower inner cases,
         * but you should understand that this will your tests harder to change
         * if you decide to change the behavior of the {@code TaskCrudController} class.
         */
        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderTaskListView() throws Exception {
            mockMvc.perform(get("/"))
                    .andExpect(view().name(WebTestConstants.View.TASK_LIST));
        }

        public class WhenNoTasksIsFound {

            @Before
            public void returnEmptyTaskList() {
                given(crudService.findAll()).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldShowEmptyTaskList() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)));
            }
        }

        public class WhenTwoTasksAreFound {

            private final Long SECOND_TASK_ID = 33L;
            private final String SECOND_TASK_TITLE = "secondTask";

            TaskListDTO first;
            TaskListDTO second;

            @Before
            public void returnTwoTasks() {
                first = createTask(TASK_ID, TASK_TITLE, TaskStatus.OPEN);
                second = createTask(SECOND_TASK_ID, SECOND_TASK_TITLE, TaskStatus.OPEN);

                given(crudService.findAll()).willReturn(Arrays.asList(first, second));
            }

            /**
             * This factory method is found from the {@code TaskCrudControllerTest} class as well.
             * I used copy paste because I think that it makes these tests easier to read. However,
             * if there would be a third test class that requires this method, I would move it into
             * an object mother class.
             * <p>
             * Also, I didn't use factory methods or test data builder because the {@code TaskListDTO}
             * objects are just dummy data containers.
             */
            private TaskListDTO createTask(Long id, String title, TaskStatus status) {
                TaskListDTO task = new TaskListDTO();

                task.setId(id);
                task.setTitle(title);
                task.setStatus(status);
                return task;
            }

            @Test
            public void shouldShowTaskListThatHasTwoTasks() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(2)));
            }

            @Test
            public void shouldShowTwoTasksInCorrectOrder() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, contains(first, second)));
            }

            @Test
            public void shouldShowCorrectInformation() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, allOf(
                                hasItem(allOf(
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(TASK_TITLE)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.OPEN))
                                )),
                                hasItem(allOf(
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(SECOND_TASK_ID)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(SECOND_TASK_TITLE)),
                                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.OPEN))
                                ))
                        )));
            }
        }
    }

    public class ShowUpdateTaskForm {

        public class WhenUpdatedTaskIsNotFound {

            @Before
            public void throwTaskNotFoundException() {
                given(crudService.findById(TASK_ID)).willThrow(new NotFoundException(""));
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(status().isNotFound());
            }

            @Test
            public void shouldRenderNotFoundView() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND));
            }
        }

        public class WhenUpdatedTaskIsFound {

            @Before
            public void returnUpdatedTask() {
                PersonDTO creator = new PersonDTO();
                creator.setName(CREATOR_NAME);
                creator.setUserId(CREATOR_ID);

                TaskDTO found = new TaskDTOBuilder()
                        .withCreator(creator)
                        .withDescription(TASK_DESCRIPTION)
                        .withId(TASK_ID)
                        .withStatusOpen()
                        .withTitle(TASK_TITLE)
                        .build();
                given(crudService.findById(TASK_ID)).willReturn(found);
            }

            @Test
            public void shouldReturnHttpStatusCodeOk() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(status().isOk());
            }

            @Test
            public void shouldRenderUpdateTaskView() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(view().name(WebTestConstants.View.UPDATE_TASK));
            }

            @Test
            public void shouldShowInformationOfUpdatedTask() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID)),
                                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(TASK_TITLE))
                        )));
            }
        }
    }

    public class ProcessUpdateTaskForm {

        /**
         * Remember that you cannot test all possible combinations because it would
         * take too long. You should pick the combinations that are easy to test
         * and test them.
         */
        public class WhenValidationFails {

            public class WhenEmptyFormIsSubmitted {

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderUpdateTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(view().name(WebTestConstants.View.UPDATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForEmptyTitle() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                                    WebTestConstants.ModelAttributeProperty.Task.TITLE,
                                    is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)
                            ));
                }

                @Test
                public void shouldShowEmptyTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is("")),
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(""))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID))
                            ));
                }

                @Test
                public void shouldNotUpdateTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, "")
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, "")
                    );

                    verify(crudService, never()).update(isA(TaskFormDTO.class), isA(LoggedInUser.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongDescription {

                private String maxLengthTitle;
                private String tooLongDescription;

                @Before
                public void createTitleAndDescription() {
                    maxLengthTitle = TestStringUtil.createStringWithLength(MAX_LENGTH_TITLE);
                    tooLongDescription = TestStringUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderUpdateTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(WebTestConstants.View.UPDATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                                    WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(tooLongDescription)),
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(maxLengthTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID))
                            ));
                }

                @Test
                public void shouldNotUpdateTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, tooLongDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    );

                    verify(crudService, never()).update(isA(TaskFormDTO.class), isA(LoggedInUser.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongTitle {

                private String tooLongTitle;
                private String maxLengthDescription;

                @Before
                public void createTitleAndDescription() {
                    tooLongTitle = TestStringUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                    maxLengthDescription = TestStringUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderUpdateTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(view().name(WebTestConstants.View.UPDATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongTitle() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(WebTestConstants.ModelAttributeName.TASK,
                                    WebTestConstants.ModelAttributeProperty.Task.TITLE,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK, allOf(
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, is(maxLengthDescription)),
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(tooLongTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK,
                                    hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(TASK_ID))
                            ));
                }

                @Test
                public void shouldNotUpdateTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, tooLongTitle)
                    );

                    verify(crudService, never()).update(isA(TaskFormDTO.class), isA(LoggedInUser.class));
                }
            }
        }

        public class WhenValidationIsSuccessful {

            private String maxLengthDescription = "updatedDescription";
            private String maxLengthTitle = "updatedTitle";

            @Before
            public void createTitleAndDescription() {
                maxLengthDescription = TestStringUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                maxLengthTitle = TestStringUtil.createStringWithLength(MAX_LENGTH_TITLE);
            }

            public class WhenUpdatedTaskIsNotFound {

                @Before
                public void throwNotFoundException() {
                    given(crudService.update(isA(TaskFormDTO.class), isA(LoggedInUser.class))).willThrow(new NotFoundException(""));
                }

                @Test
                public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isNotFound());
                }

                @Test
                public void shouldRenderNotFoundView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(WebTestConstants.ErrorView.NOT_FOUND));
                }
            }

            public class WhenUpdatedTaskIsFound {

                private static final String FEEDBACK_MESSAGE_KEY_TASK_UPDATED = "feedback.message.task.updated";
                private static final String FEEDBACK_MESSAGE_TASK_UPDATED = "Task updated";

                /**
                 * You should consider dividing the configuration of the test cases into multiple private
                 * methods if the configuration has multiple different steps.
                 */
                @Before
                public void configureTestCases() {
                    returnUpdatedTask();
                    returnFeedbackMessage();
                }

                private void returnUpdatedTask() {
                    PersonDTO creator = new PersonDTO();
                    creator.setName(CREATOR_NAME);
                    creator.setUserId(CREATOR_ID);

                    TaskDTO updated = new TaskDTOBuilder()
                            .withId(TASK_ID)
                            .withCreator(creator)
                            .withTitle(maxLengthTitle)
                            .withDescription(maxLengthDescription)
                            .withStatusOpen()
                            .build();
                    given(crudService.update(isA(TaskFormDTO.class), isA(LoggedInUser.class))).willReturn(updated);
                }

                private void returnFeedbackMessage() {
                    messageSource.addMessage(FEEDBACK_MESSAGE_KEY_TASK_UPDATED,
                            WebTestConfig.LOCALE,
                            FEEDBACK_MESSAGE_TASK_UPDATED
                    );
                }

                @Test
                public void shouldReturnHttpStatusCodeFound() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isFound());
                }

                @Test
                public void shouldRedirectUserToViewTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(WebTestConstants.RedirectView.SHOW_TASK))
                            .andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_ID, is(TASK_ID.toString())));
                }

                @Test
                public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    )
                            .andExpect(flash().attribute(WebTestConstants.FlashMessageKey.FEEDBACK_MESSAGE,
                                    FEEDBACK_MESSAGE_TASK_UPDATED
                            ));
                }

                @Test
                public void shouldUpdateDescriptionOfTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    );

                    verify(crudService, times(1)).update(assertArg(
                                task -> assertThat(task.getDescription()).isEqualTo(maxLengthDescription)
                            ),
                            isA(LoggedInUser.class)
                    );
                }

                @Test
                public void shouldUpdateTaskWithCorrectId() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    );

                    verify(crudService, times(1)).update(assertArg(
                                task -> assertThat(task.getId()).isEqualTo(TASK_ID)
                            ),
                            isA(LoggedInUser.class)
                    );
                }

                @Test
                public void shouldUpdateTitleOfTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(WebTestConstants.ModelAttributeProperty.Task.ID, TASK_ID.toString())
                            .param(WebTestConstants.ModelAttributeProperty.Task.DESCRIPTION, maxLengthDescription)
                            .param(WebTestConstants.ModelAttributeProperty.Task.TITLE, maxLengthTitle)
                    );

                    verify(crudService, times(1)).update(assertArg(
                                task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                            ),
                            isA(LoggedInUser.class)
                    );
                }
            }
        }
    }
}
