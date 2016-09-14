package com.testwithspring.starter.springboot.web;

import com.testwithspring.starter.springboot.TaskDTOBuilder;
import com.testwithspring.starter.springboot.UnitTest;
import com.testwithspring.starter.springboot.task.TaskCrudService;
import com.testwithspring.starter.springboot.task.TaskDTO;
import com.testwithspring.starter.springboot.task.TaskFormDTO;
import com.testwithspring.starter.springboot.task.TaskListDTO;
import com.testwithspring.starter.springboot.task.TaskNotFoundException;
import com.testwithspring.starter.springboot.task.TaskStatus;
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

import static com.testwithspring.starter.springboot.web.WebTestConfig.exceptionResolver;
import static com.testwithspring.starter.springboot.web.WebTestConfig.fixedLocaleResolver;
import static com.testwithspring.starter.springboot.web.WebTestConfig.viewResolver;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class TaskCrudControllerTest {

    //Feedback messages
    private static final String FLASH_MESSAGE_KEY_FEEDBACK = "feedbackMessage";

    //Validation
    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    private static final String VALIDATION_ERROR_CODE_EMPTY_FIELD = "NotBlank";
    private static final String VALIDATION_ERROR_CODE_LONG_FIELD_VALUE = "Size";

    //Form fields / model attribute fields
    private static final String TASK_PROPERTY_NAME_ASSIGNEE = "assigneeId";
    private static final String TASK_PROPERTY_NAME_CLOSER = "closerId";
    private static final String TASK_PROPERTY_NAME_CREATOR = "creatorId";
    private static final String TASK_PROPERTY_NAME_DESCRIPTION = "description";
    private static final String TASK_PROPERTY_NAME_ID = "id";
    private static final String TASK_PROPERTY_NAME_RESOLUTION = "resolution";
    private static final String TASK_PROPERTY_NAME_STATUS = "status";
    private static final String TASK_PROPERTY_NAME_TITLE = "title";

    //Model attributes
    private static final String MODEL_ATTRIBUTE_TASK_ID = "taskId";
    private static final String MODEL_ATTRIBUTE_NAME_TASK = "task";
    private static final String MODEL_ATTRIBUTE_NAME_TASKS = "tasks";

    //Views
    private static final String ERROR_VIEW_NAME_NOT_FOUND = "error/404";
    private static final String VIEW_NAME_CREATE_TASK = "task/create";
    private static final String VIEW_NAME_TASK_LIST = "task/list";
    private static final String VIEW_NAME_UPDATE_TASK = "task/update";
    private static final String VIEW_NAME_VIEW_TASK = "task/view";
    private static final String REDIRECT_VIEW_TASK_LIST = "redirect:/";
    private static final String REDIRECT_VIEW_TASK = "redirect:/task/{taskId}";

    //Task
    private static final Long CREATOR_ID = 99L;
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
                given(crudService.delete(TASK_ID)).willThrow(new TaskNotFoundException());
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(status().isNotFound());
            }

            @Test
            public void shouldRenderNotFoundView() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(view().name(ERROR_VIEW_NAME_NOT_FOUND));
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
            public void shouldRedirectUserToViewTaskView() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(view().name(REDIRECT_VIEW_TASK_LIST));
            }

            @Test
            public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
                mockMvc.perform(get("/task/{taskId}/delete", TASK_ID))
                        .andExpect(flash().attribute(FLASH_MESSAGE_KEY_FEEDBACK, FEEDBACK_MESSAGE_TASK_DELETED));
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
                    .andExpect(view().name(VIEW_NAME_CREATE_TASK));
        }

        @Test
        public void shouldCreateAnEmptyFormObject() throws Exception {
            mockMvc.perform(get("/task/create"))
                    .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                            hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, nullValue()),
                            hasProperty(TASK_PROPERTY_NAME_ID, nullValue()),
                            hasProperty(TASK_PROPERTY_NAME_TITLE, nullValue())
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
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderCreateTaskView() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(view().name(VIEW_NAME_CREATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForEmptyTitle() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTRIBUTE_NAME_TASK,
                                    TASK_PROPERTY_NAME_TITLE,
                                    is(VALIDATION_ERROR_CODE_EMPTY_FIELD)
                            ));
                }

                @Test
                public void shouldShowEmptyTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                    hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is("")),
                                    hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(""))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, hasProperty(TASK_PROPERTY_NAME_ID, nullValue())));
                }

                @Test
                public void shouldNotCreateNewTask() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    );

                    verify(crudService, never()).create(isA(TaskFormDTO.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongDescription {

                private String maxLengthTitle;
                private String tooLongDescription;

                @Before
                public void createTitleAndDescription() {
                    maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                    tooLongDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderCreateTaskView() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(VIEW_NAME_CREATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTRIBUTE_NAME_TASK,
                                    TASK_PROPERTY_NAME_DESCRIPTION,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                    hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(tooLongDescription)),
                                    hasProperty(TASK_PROPERTY_NAME_TITLE, is(maxLengthTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, hasProperty(TASK_PROPERTY_NAME_ID, nullValue())));
                }

                @Test
                public void shouldNotCreateNewTask() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    );

                    verify(crudService, never()).create(isA(TaskFormDTO.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongTitle {

                private String tooLongTitle;
                private String maxLengthDescription;

                @Before
                public void createTitleAndDescription() {
                    tooLongTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                    maxLengthDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderCreateTaskView() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(view().name(VIEW_NAME_CREATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongTitle() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTRIBUTE_NAME_TASK,
                                    TASK_PROPERTY_NAME_TITLE,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                    hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(maxLengthDescription)),
                                    hasProperty(TASK_PROPERTY_NAME_TITLE, is(tooLongTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, hasProperty(TASK_PROPERTY_NAME_ID, nullValue())));
                }

                @Test
                public void shouldNotCreateNewTask() throws Exception {
                    mockMvc.perform(post("/task/create")
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    );

                    verify(crudService, never()).create(isA(TaskFormDTO.class));
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
                maxLengthDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
            }

            private void returnCreatedTask() {
                TaskDTO created = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(CREATOR_ID)
                        .withTitle(maxLengthTitle)
                        .withDescription(maxLengthDescription)
                        .withStatusOpen()
                        .build();
                given(crudService.create(isA(TaskFormDTO.class))).willReturn(created);
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
                        .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                        .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                )
                        .andExpect(status().isFound());
            }

            @Test
            public void shouldRedirectUserToViewTaskView() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                        .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                )
                        .andExpect(view().name(REDIRECT_VIEW_TASK))
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_TASK_ID, is(TASK_ID.toString())));
            }

            @Test
            public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                        .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                )
                        .andExpect(flash().attribute(FLASH_MESSAGE_KEY_FEEDBACK, FEEDBACK_MESSAGE_TASK_CREATED));
            }

            @Test
            public void shouldCreateNewTaskWithCorrectDescription() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                        .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                );

                verify(crudService, times(1)).create(assertArg(
                        task -> assertThat(task.getDescription()).isEqualTo(maxLengthDescription)
                ));
            }

            @Test
            public void shouldCreateNewTaskWithNullId() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                        .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                );

                verify(crudService, times(1)).create(assertArg(
                        task -> assertThat(task.getId()).isNull()
                ));
            }

            @Test
            public void shouldCreateNewTaskWithCorrectTitle() throws Exception {
                mockMvc.perform(post("/task/create")
                        .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                        .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                );

                verify(crudService, times(1)).create(assertArg(
                        task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                ));
            }
        }
    }

    public class ShowTask {

        public class WhenTaskIsNotFound {

            @Before
            public void throwTaskNotFoundException() {
                given(crudService.findById(TASK_ID)).willThrow(new TaskNotFoundException());
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(status().isNotFound());
            }

            @Test
            public void shouldRenderNotFoundView() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(view().name(ERROR_VIEW_NAME_NOT_FOUND));
            }
        }

        public class WhenTaskIsFound {

            private TaskDTO found;

            @Before
            public void returnFoundTask() {
                found = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(CREATOR_ID)
                        .withTitle(TASK_TITLE)
                        .withDescription(TASK_DESCRIPTION)
                        .withStatusOpen()
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
                        .andExpect(view().name(VIEW_NAME_VIEW_TASK));
            }

            @Test
            public void shouldShowFoundTask() throws Exception {
                mockMvc.perform(get("/task/{taskId}", TASK_ID))
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                hasProperty(TASK_PROPERTY_NAME_ASSIGNEE, nullValue()),
                                hasProperty(TASK_PROPERTY_NAME_CLOSER, nullValue()),
                                hasProperty(TASK_PROPERTY_NAME_CREATOR, is(CREATOR_ID)),
                                hasProperty(TASK_PROPERTY_NAME_ID, is(TASK_ID)),
                                hasProperty(TASK_PROPERTY_NAME_TITLE, is(TASK_TITLE)),
                                hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(TASK_DESCRIPTION)),
                                hasProperty(TASK_PROPERTY_NAME_STATUS, is(TaskStatus.OPEN)),
                                hasProperty(TASK_PROPERTY_NAME_RESOLUTION, nullValue())
                        )));
            }
        }
    }

    public class ShowTaskList {

        /**
         * These two test methods are added into this test class because both of them
         * test behavior that should happen in every case (tasks are not found and
         * tasks are not found).
         *
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
                    .andExpect(view().name(VIEW_NAME_TASK_LIST));
        }

        public class WhenNoTasksIsFound {

            @Before
            public void returnEmptyTaskList() {
                given(crudService.findAll()).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldShowEmptyTaskList() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, hasSize(0)));
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
             *
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
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, hasSize(2)));
            }

            @Test
            public void shouldShowTwoTasksInCorrectOrder() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, contains(first, second)));
            }

            @Test
            public void shouldShowCorrectInformation() throws Exception {
                mockMvc.perform(get("/"))
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, allOf(
                                hasItem(allOf(
                                        hasProperty(TASK_PROPERTY_NAME_ID, is(TASK_ID)),
                                        hasProperty(TASK_PROPERTY_NAME_TITLE, is(TASK_TITLE)),
                                        hasProperty(TASK_PROPERTY_NAME_STATUS, is(TaskStatus.OPEN))
                                )),
                                hasItem(allOf(
                                        hasProperty(TASK_PROPERTY_NAME_ID, is(SECOND_TASK_ID)),
                                        hasProperty(TASK_PROPERTY_NAME_TITLE, is(SECOND_TASK_TITLE)),
                                        hasProperty(TASK_PROPERTY_NAME_STATUS, is(TaskStatus.OPEN))
                                ))
                        )));
            }
        }
    }

    public class ShowUpdateTaskForm {

        public class WhenUpdatedTaskIsNotFound {

            @Before
            public void throwTaskNotFoundException() {
                given(crudService.findById(TASK_ID)).willThrow(new TaskNotFoundException());
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(status().isNotFound());
            }

            @Test
            public void shouldRenderNotFoundView() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(view().name(ERROR_VIEW_NAME_NOT_FOUND));
            }
        }

        public class WhenUpdatedTaskIsFound {

            @Before
            public void returnUpdatedTask() {
                TaskDTO found = new TaskDTOBuilder()
                        .withCreator(CREATOR_ID)
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
                        .andExpect(view().name(VIEW_NAME_UPDATE_TASK));
            }

            @Test
            public void shouldShowInformationOfUpdatedTask() throws Exception {
                mockMvc.perform(get("/task/{taskId}/update", TASK_ID))
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(TASK_DESCRIPTION)),
                                hasProperty(TASK_PROPERTY_NAME_ID, is(TASK_ID)),
                                hasProperty(TASK_PROPERTY_NAME_TITLE, is(TASK_TITLE))
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
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderUpdateTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(view().name(VIEW_NAME_UPDATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForEmptyTitle() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTRIBUTE_NAME_TASK,
                                    TASK_PROPERTY_NAME_TITLE,
                                    is(VALIDATION_ERROR_CODE_EMPTY_FIELD)
                            ));
                }

                @Test
                public void shouldShowEmptyTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                    hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is("")),
                                    hasProperty(TASK_PROPERTY_NAME_TITLE, is(""))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK,
                                    hasProperty(TASK_PROPERTY_NAME_ID, is(TASK_ID))
                            ));
                }

                @Test
                public void shouldNotUpdateTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, "")
                            .param(TASK_PROPERTY_NAME_TITLE, "")
                    );

                    verify(crudService, never()).update(isA(TaskFormDTO.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongDescription {

                private String maxLengthTitle;
                private String tooLongDescription;

                @Before
                public void createTitleAndDescription() {
                    maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                    tooLongDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderUpdateTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(VIEW_NAME_UPDATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTRIBUTE_NAME_TASK,
                                    TASK_PROPERTY_NAME_DESCRIPTION,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                    hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(tooLongDescription)),
                                    hasProperty(TASK_PROPERTY_NAME_TITLE, is(maxLengthTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK,
                                    hasProperty(TASK_PROPERTY_NAME_ID, is(TASK_ID))
                            ));
                }

                @Test
                public void shouldNotUpdateTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, tooLongDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    );

                    verify(crudService, never()).update(isA(TaskFormDTO.class));
                }
            }

            public class WhenFormIsSubmittedWithTooLongTitle {

                private String tooLongTitle;
                private String maxLengthDescription;

                @Before
                public void createTitleAndDescription() {
                    tooLongTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                    maxLengthDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldRenderUpdateTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(view().name(VIEW_NAME_UPDATE_TASK));
                }

                @Test
                public void shouldShowValidationErrorForTooLongTitle() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(model().attributeHasFieldErrorCode(MODEL_ATTRIBUTE_NAME_TASK,
                                    TASK_PROPERTY_NAME_TITLE,
                                    is(VALIDATION_ERROR_CODE_LONG_FIELD_VALUE)
                            ));
                }

                @Test
                public void shouldShowEnteredTitleAndDescription() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK, allOf(
                                    hasProperty(TASK_PROPERTY_NAME_DESCRIPTION, is(maxLengthDescription)),
                                    hasProperty(TASK_PROPERTY_NAME_TITLE, is(tooLongTitle))
                            )));
                }

                @Test
                public void shouldNotModifyIdField() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    )
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASK,
                                    hasProperty(TASK_PROPERTY_NAME_ID, is(TASK_ID))
                            ));
                }

                @Test
                public void shouldNotUpdateTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, tooLongTitle)
                    );

                    verify(crudService, never()).update(isA(TaskFormDTO.class));
                }
            }
        }

        public class WhenValidationIsSuccessful {

            private String maxLengthDescription = "updatedDescription";
            private String maxLengthTitle = "updatedTitle";

            @Before
            public void createTitleAndDescription() {
                maxLengthDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
            }

            public class WhenUpdatedTaskIsNotFound {

                @Before
                public void throwNotFoundException() {
                    given(crudService.update(isA(TaskFormDTO.class))).willThrow(new TaskNotFoundException());
                }

                @Test
                public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isNotFound());
                }

                @Test
                public void shouldRenderNotFoundView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(ERROR_VIEW_NAME_NOT_FOUND));
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
                    TaskDTO updated = new TaskDTOBuilder()
                            .withId(TASK_ID)
                            .withCreator(CREATOR_ID)
                            .withTitle(maxLengthTitle)
                            .withDescription(maxLengthDescription)
                            .withStatusOpen()
                            .build();
                    given(crudService.update(isA(TaskFormDTO.class))).willReturn(updated);
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
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(status().isFound());
                }

                @Test
                public void shouldRedirectUserToViewTaskView() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(view().name(REDIRECT_VIEW_TASK))
                            .andExpect(model().attribute(MODEL_ATTRIBUTE_TASK_ID, is(TASK_ID.toString())));
                }

                @Test
                public void shouldAddFeedbackMessageAsAFlashAttribute() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    )
                            .andExpect(flash().attribute(FLASH_MESSAGE_KEY_FEEDBACK, FEEDBACK_MESSAGE_TASK_UPDATED));
                }

                @Test
                public void shouldUpdateDescriptionOfTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    );

                    verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getDescription()).isEqualTo(maxLengthDescription)
                    ));
                }

                @Test
                public void shouldUpdateTaskWithCorrectId() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    );

                    verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getId()).isEqualTo(TASK_ID)
                    ));
                }

                @Test
                public void shouldUpdateTitleOfTask() throws Exception {
                    mockMvc.perform(post("/task/{taskId}/update", TASK_ID)
                            .param(TASK_PROPERTY_NAME_ID, TASK_ID.toString())
                            .param(TASK_PROPERTY_NAME_DESCRIPTION, maxLengthDescription)
                            .param(TASK_PROPERTY_NAME_TITLE, maxLengthTitle)
                    );

                    verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                    ));
                }
            }
        }
    }
}
