package com.testwithspring.intermediate.web;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static com.testwithspring.intermediate.web.WebTestConfig.fixedLocaleResolver;
import static com.testwithspring.intermediate.web.WebTestConfig.objectMapperHttpMessageConverter;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class TaskCrudControllerTest {

    //Validation
    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_TITLE = 100;

    //JSON Fields
    private static final String JSON_FIELD_DESCRIPTION = "description";
    private static final String JSON_FIELD_TITLE = "title";

    //Task
    private static final Long CREATOR_ID = 99L;
    private static final String CREATOR_NAME = "John Doe";
    private static final String TASK_DESCRIPTION = "description";
    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "maxLengthTitle";

    private static final Long TAG_ID = 44L;
    private static final String TAG_NAME = "testing";

    private TaskCrudService crudService;
    private MockMvc mockMvc;

    /**
     * You need to configure all these custom components because they are
     * required if you want to write unit tests for the {@code TaskCrudController}
     * class.
     */
    @Before
    public void configureSystemUnderTest() {
        crudService = mock(TaskCrudService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new TaskCrudController(crudService))
                .setControllerAdvice(new TaskErrorHandler())
                .setLocaleResolver(fixedLocaleResolver())
                .setMessageConverters(objectMapperHttpMessageConverter())
                .build();
    }

    public class Create {

        private TaskFormDTO input;

        public class WhenFieldsAreEmpty {

            @Before
            public void createEmptyInput() {
                input = new TaskFormDTO();
                input.setDescription("");
                input.setTitle("");
            }

            @Test
            public void shouldReturnHttpStatusCodeBadRequest() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isBadRequest());
            }

            @Test
            public void shouldReturnValidationErrorsAsJson() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnOneValidationError() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors", hasSize(1)));
            }

            @Test
            public void shouldReturnValidationErrorAboutEmptyTitle() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors[0].field", is(JSON_FIELD_TITLE)))
                        .andExpect(jsonPath("$.fieldErrors[0].errorCode", is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)));
            }

            @Test
            public void shouldNotCreateTask() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, never()).create(isA(TaskFormDTO.class), isA(LoggedInUser.class));
            }
        }

        public class WhenTitleAndDescriptionAreTooLong {

            @Before
            public void createInputWithTooLongFields() {
                input = new TaskFormDTO();

                String title = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                input.setTitle(title);

                String description = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
                input.setDescription(description);
            }

            @Test
            public void shouldReturnHttpStatusCodeBadRequest() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isBadRequest());
            }

            @Test
            public void shouldReturnValidationErrorsAsJson() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnTwoValidationErrors() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors", hasSize(2)));
            }

            @Test
            public void shouldReturnValidationErrorsAboutTooLongTitleAndDescription() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors[?(@.field == 'title')].errorCode",
                                contains(WebTestConstants.ValidationErrorCode.TOO_LONG_FIELD)
                        ))
                        .andExpect(jsonPath("$.fieldErrors[?(@.field == 'description')].errorCode",
                                contains(WebTestConstants.ValidationErrorCode.TOO_LONG_FIELD)
                        ));
            }

            @Test
            public void shouldNotCreateTask() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, never()).create(isA(TaskFormDTO.class), isA(LoggedInUser.class));
            }
        }

        public class WhenDescriptionIsEmpty {

            private String maxLengthTitle;

            @Before
            public void configureTestCases() {
                createValidInput();
                returnCreatedTask();
            }

            private void createValidInput() {
                input = new TaskFormDTO();

                maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                input.setTitle(maxLengthTitle);

                input.setDescription("");
            }

            private void returnCreatedTask() {
                PersonDTO creator = new PersonDTO();
                creator.setUserId(CREATOR_ID);
                creator.setName(CREATOR_NAME);

                TaskDTO created = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(creator)
                        .withModifier(creator)
                        .withTitle(maxLengthTitle)
                        .withDescription("")
                        .withStatusOpen()
                        .build();
                given(crudService.create(isA(TaskFormDTO.class), isA(LoggedInUser.class))).willReturn(created);
            }

            @Test
            public void shouldReturnHttpStatusCodeCreated() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isCreated());
            }

            @Test
            public void shouldReturnCreatedTaskWithJson() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnCorrectInformation() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(maxLengthTitle)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, isEmptyString()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()));
            }

            @Test
            public void shouldCreateTaskWithoutId() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getId()).isNull()
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldCreateTaskWithCorrectTitle() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldCreateTaskWithEmptyDescription() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getDescription()).isEmpty()
                        ),
                        isA(LoggedInUser.class)
                );
            }
        }

        public class WhenAllFieldsAreValid {

            private String maxLengthTitle;
            private String maxLengthDescription;

            @Before
            public void configureTestCases() {
                createValidInput();
                returnCreatedTask();
            }

            private void createValidInput() {
                input = new TaskFormDTO();

                maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                input.setTitle(maxLengthTitle);

                maxLengthDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                input.setDescription(maxLengthDescription);
            }

            private void returnCreatedTask() {
                PersonDTO creator = new PersonDTO();
                creator.setUserId(CREATOR_ID);
                creator.setName(CREATOR_NAME);

                TaskDTO created = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(creator)
                        .withModifier(creator)
                        .withTitle(maxLengthTitle)
                        .withDescription(maxLengthDescription)
                        .withStatusOpen()
                        .build();
                given(crudService.create(isA(TaskFormDTO.class), isA(LoggedInUser.class))).willReturn(created);
            }

            @Test
            public void shouldReturnHttpStatusCodeCreated() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isCreated());
            }

            @Test
            public void shouldReturnCreatedTaskWithJson() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnInformationOfCreatedTask() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(maxLengthTitle)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(maxLengthDescription)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()));
            }

            @Test
            public void shouldReturnTaskThatHasZeroTags() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(0)));
            }

            @Test
            public void shouldCreateTaskWithoutId() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getId()).isNull()
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldCreateTaskWithCorrectTitle() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldCreateTaskWithCorrectDescription() throws Exception {
                mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).create(assertArg(
                            task -> assertThat(task.getDescription()).isEqualTo(maxLengthDescription)
                        ),
                        isA(LoggedInUser.class)
                );
            }
        }
    }

    public class Delete {

        public class WhenTaskIsNotFound {

            @Before
            public void throwTaskNotFoundException() {
                given(crudService.delete(TASK_ID)).willThrow(new NotFoundException(""));
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(delete("/api/task/{taskId}", TASK_ID))
                        .andExpect(status().isNotFound());
            }
        }

        public class WhenTaskIsFound {

            @Before
            public void returnDeletedTask() {
                TagDTO tag = new TagDTO();
                tag.setId(TAG_ID);
                tag.setName(TAG_NAME);

                PersonDTO creator = new PersonDTO();
                creator.setUserId(CREATOR_ID);
                creator.setName(CREATOR_NAME);

                TaskDTO deleted = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(creator)
                        .withModifier(creator)
                        .withTitle(TASK_TITLE)
                        .withDescription(TASK_DESCRIPTION)
                        .withStatusOpen()
                        .withTags(tag)
                        .build();

                given(crudService.delete(TASK_ID)).willReturn(deleted);
            }

            @Test
            public void shouldReturnHttpStatusCodeOk() throws Exception {
                mockMvc.perform(delete("/api/task/{taskId}", TASK_ID))
                        .andExpect(status().isOk());
            }

            @Test
            public void shouldReturnDeletedTaskAsJson() throws Exception {
                mockMvc.perform(delete("/api/task/{taskId}", TASK_ID))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnInformationOfDeletedTask() throws Exception {
                mockMvc.perform(delete("/api/task/{taskId}", TASK_ID))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(TASK_TITLE)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()));
            }

            @Test
            public void shouldReturnTaskThatHasOneTag() throws Exception {
                mockMvc.perform(delete("/api/task/{taskId}", TASK_ID))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)));
            }

            @Test
            public void shouldReturnTagOfDeletedTask() throws Exception {
                mockMvc.perform(delete("/api/task/{taskId}", TASK_ID))
                        .andExpect(jsonPath("$.tags[0].id", is(TAG_ID.intValue())))
                        .andExpect(jsonPath("$.tags[0].name", is(TAG_NAME)));
            }
        }
    }

    public class FindAll {

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/api/task"))
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldReturnFoundTaskAsJson() throws Exception {
            mockMvc.perform(get("/api/task"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        }

        public class WhenNoTasksIsFound {

            @Before
            public void returnEmptyList() {
                given(crudService.findAll()).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldReturnEmptyTaskList() throws Exception {
                mockMvc.perform(get("/api/task"))
                        .andExpect(jsonPath("$", hasSize(0)));
            }
        }

        public class WhenTwoTasksAreFound {

            private final Long SECOND_TASK_ID = 33L;
            private final String SECOND_TASK_TITLE = "secondTask";

            @Before
            public void returnTwoTasks() {
                TaskListDTO first = createTask(TASK_ID, TASK_TITLE, TaskStatus.OPEN);
                TaskListDTO second = createTask(SECOND_TASK_ID, SECOND_TASK_TITLE, TaskStatus.OPEN);

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
            public void shouldReturnListThatHasTwoTasks() throws Exception {
                mockMvc.perform(get("/api/task"))
                        .andExpect(jsonPath("$", hasSize(2)));
            }

            @Test
            public void shouldReturnCorrectTasks() throws Exception {
                mockMvc.perform(get("/api/task"))
                        .andExpect(jsonPath("$[0].id", is(TASK_ID.intValue())))
                        .andExpect(jsonPath("$[0].title", is(TASK_TITLE)))
                        .andExpect(jsonPath("$[0].status", is(TaskStatus.OPEN.toString())))
                        .andExpect(jsonPath("$[1].id", is(SECOND_TASK_ID.intValue())))
                        .andExpect(jsonPath("$[1].title", is(SECOND_TASK_TITLE)))
                        .andExpect(jsonPath("$[1].status", is(TaskStatus.OPEN.toString())));
            }
        }
    }

    public class FindById {

        public class WhenTaskIsNotFound {

            @Before
            public void throwTaskNotFoundException() {
                given(crudService.findById(TASK_ID)).willThrow(new NotFoundException(""));
            }

            @Test
            public void shouldReturnHttpStatusCodeNotFound() throws Exception {
                mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                        .andExpect(status().isNotFound());
            }
        }

        public class WhenTaskIsFound {

            private PersonDTO creator;
            private TagDTO tag;

            @Before
            public void createTestData() {
                creator = new PersonDTO();
                creator.setUserId(CREATOR_ID);
                creator.setName(CREATOR_NAME);

                tag = new TagDTO();
                tag.setId(TAG_ID);
                tag.setName(TAG_NAME);
            }

            public class WhenOpenTaskIsFound {

                @Before
                public void returnFoundTask() {
                    TaskDTO found = new TaskDTOBuilder()
                            .withId(TASK_ID)
                            .withCreator(creator)
                            .withModifier(creator)
                            .withTitle(TASK_TITLE)
                            .withDescription(TASK_DESCRIPTION)
                            .withStatusOpen()
                            .withTags(tag)
                            .build();

                    given(crudService.findById(TASK_ID)).willReturn(found);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldReturnFoundTaskAsJson() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
                }

                @Test
                public void shouldReturnInformationOfFoundTask() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(CREATOR_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(CREATOR_NAME)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(TASK_TITLE)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()));
                }


                @Test
                public void shouldReturnTaskThatHasOneTag() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)));
                }

                @Test
                public void shouldReturnTagOfFoundTask() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(jsonPath("$.tags[0].id", is(TAG_ID.intValue())))
                            .andExpect(jsonPath("$.tags[0].name", is(TAG_NAME)));
                }
            }

            public class WhenClosedTaskIsFound {

                private final Long CLOSER_ID = 124L;
                private final String CLOSER_NAME = "Chris Closer";

                @Before
                public void returnFoundTask() {
                    PersonDTO closer = new PersonDTO();
                    closer.setUserId(CLOSER_ID);
                    closer.setName(CLOSER_NAME);

                    TaskDTO found = new TaskDTOBuilder()
                            .withId(TASK_ID)
                            .withAssignee(closer)
                            .withCreator(creator)
                            .withModifier(creator)
                            .withTitle(TASK_TITLE)
                            .withDescription(TASK_DESCRIPTION)
                            .withResolutionWontDo(closer)
                            .withTags(tag)
                            .build();

                    given(crudService.findById(TASK_ID)).willReturn(found);
                }

                @Test
                public void shouldReturnHttpStatusCodeOk() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(status().isOk());
                }

                @Test
                public void shouldReturnFoundTaskAsJson() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
                }

                @Test
                public void shouldReturnInformationOfFoundTask() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.ID, is(CLOSER_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Assignee.NAME, is(CLOSER_NAME)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.ID, is(CLOSER_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Closer.NAME, is(CLOSER_NAME)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(CREATOR_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(CREATOR_NAME)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(TASK_TITLE)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(TASK_DESCRIPTION)))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.CLOSED.toString())))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, is(TaskResolution.WONT_DO.toString())));
                }


                @Test
                public void shouldReturnTaskThatHasOneTag() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)));
                }

                @Test
                public void shouldReturnTagOfFoundTask() throws Exception {
                    mockMvc.perform(get("/api/task/{taskId}", TASK_ID))
                            .andExpect(jsonPath("$.tags[0].id", is(TAG_ID.intValue())))
                            .andExpect(jsonPath("$.tags[0].name", is(TAG_NAME)));
                }
            }
        }
    }

    public class Update {

        private final Long MODIFIER_ID = 44L;
        private final String MODIFIER_NAME = "Mike Modifier";

        private TaskFormDTO input;

        public class WhenFieldsAreEmpty {

            @Before
            public void createEmptyInput() {
                input = new TaskFormDTO();
                input.setId(TASK_ID);
                input.setTitle("");
                input.setDescription("");
            }

            @Test
            public void shouldReturnHttpStatusCodeBadRequest() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isBadRequest());
            }

            @Test
            public void shouldReturnValidationErrorsAsJson() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnOneValidationError() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors", hasSize(1)));
            }

            @Test
            public void shouldReturnValidationErrorAboutEmptyTitle() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors[0].field", is(JSON_FIELD_TITLE)))
                        .andExpect(jsonPath("$.fieldErrors[0].errorCode", is(WebTestConstants.ValidationErrorCode.EMPTY_FIELD)));
            }

            @Test
            public void shouldNotCreateTask() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, never()).create(isA(TaskFormDTO.class), isA(LoggedInUser.class));
            }
        }

        public class WhenTitleAndDescriptionAreTooLong {

            @Before
            public void createInputWithTooLongFields() {
                input = new TaskFormDTO();
                input.setId(TASK_ID);

                String title = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE + 1);
                input.setTitle(title);

                String description = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1);
                input.setDescription(description);
            }

            @Test
            public void shouldReturnHttpStatusCodeBadRequest() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isBadRequest());
            }

            @Test
            public void shouldReturnValidationErrorsAsJson() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnTwoValidationErrors() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors", hasSize(2)));
            }

            @Test
            public void shouldReturnValidationErrorsAboutTooLongTitleAndDescription() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors[?(@.field == 'title')].errorCode",
                                contains(WebTestConstants.ValidationErrorCode.TOO_LONG_FIELD)
                        ))
                        .andExpect(jsonPath("$.fieldErrors[?(@.field == 'description')].errorCode",
                                contains(WebTestConstants.ValidationErrorCode.TOO_LONG_FIELD)
                        ));
            }

            @Test
            public void shouldNotCreateTask() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, never()).create(isA(TaskFormDTO.class), isA(LoggedInUser.class));
            }
        }

        public class WhenDescriptionIsEmpty {

            private String maxLengthTitle;

            @Before
            public void configureTestCases() {
                createValidInput();
                returnUpdatedTask();
            }

            private void createValidInput() {
                input = new TaskFormDTO();
                input.setId(TASK_ID);

                maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                input.setTitle(maxLengthTitle);

                input.setDescription("");
            }

            private void returnUpdatedTask() {
                PersonDTO creator = new PersonDTO();
                creator.setUserId(CREATOR_ID);
                creator.setName(CREATOR_NAME);

                PersonDTO modifier = new PersonDTO();
                modifier.setUserId(MODIFIER_ID);
                modifier.setName(MODIFIER_NAME);

                TaskDTO created = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(creator)
                        .withModifier(modifier)
                        .withTitle(maxLengthTitle)
                        .withDescription("")
                        .withStatusOpen()
                        .build();
                given(crudService.update(isA(TaskFormDTO.class), isA(LoggedInUser.class))).willReturn(created);
            }

            @Test
            public void shouldReturnHttpStatusCodeOk() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isOk());
            }

            @Test
            public void shouldReturnUpdatedTaskWithJson() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnCorrectInformation() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(MODIFIER_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(MODIFIER_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(maxLengthTitle)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, isEmptyString()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()));
            }

            @Test
            public void shouldUpdateTaskWithCorrectId() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getId()).isEqualByComparingTo(TASK_ID)
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldUpdateTaskWithCorrectTitle() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldUpdateTaskWithEmptyDescription() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getDescription()).isEmpty()
                        ),
                        isA(LoggedInUser.class)
                );
            }
        }

        public class WhenAllFieldsAreValid {

            private String maxLengthTitle;
            private String maxLengthDescription;

            @Before
            public void configureTestCases() {
                createValidInput();
                returnUpdatedTask();
            }

            private void createValidInput() {
                input = new TaskFormDTO();
                input.setId(TASK_ID);

                maxLengthTitle = WebTestUtil.createStringWithLength(MAX_LENGTH_TITLE);
                input.setTitle(maxLengthTitle);

                maxLengthDescription = WebTestUtil.createStringWithLength(MAX_LENGTH_DESCRIPTION);
                input.setDescription(maxLengthDescription);
            }

            private void returnUpdatedTask() {
                TagDTO tag = new TagDTO();
                tag.setId(TAG_ID);
                tag.setName(TAG_NAME);

                PersonDTO creator = new PersonDTO();
                creator.setUserId(CREATOR_ID);
                creator.setName(CREATOR_NAME);

                PersonDTO modifier = new PersonDTO();
                modifier.setUserId(MODIFIER_ID);
                modifier.setName(MODIFIER_NAME);

                TaskDTO created = new TaskDTOBuilder()
                        .withId(TASK_ID)
                        .withCreator(creator)
                        .withModifier(modifier)
                        .withTitle(maxLengthTitle)
                        .withDescription(maxLengthDescription)
                        .withStatusOpen()
                        .withTags(tag)
                        .build();
                given(crudService.update(isA(TaskFormDTO.class), isA(LoggedInUser.class))).willReturn(created);
            }

            @Test
            public void shouldReturnHttpStatusCodeOk() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isOk());
            }

            @Test
            public void shouldReturnUpdatedTaskWithJson() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            public void shouldReturnInformationOfUpdatedTask() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ASSIGNEE, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.CLOSER, nullValue()))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.ID, is(CREATOR_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Creator.NAME, is(CREATOR_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.ID, is(TASK_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.ID, is(MODIFIER_ID.intValue())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.Modifier.NAME, is(MODIFIER_NAME)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TITLE, is(maxLengthTitle)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.DESCRIPTION, is(maxLengthDescription)))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.STATUS, is(TaskStatus.OPEN.toString())))
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.RESOLUTION, nullValue()));
            }

            @Test
            public void shouldReturnTaskThatHasOneTag() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath(WebTestConstants.JsonPathProperty.Task.TAGS, hasSize(1)));
            }

            @Test
            public void shouldReturnTagOfUpdatedTask()  throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.tags[0].id", is(TAG_ID.intValue())))
                        .andExpect(jsonPath("$.tags[0].name", is(TAG_NAME)));
            }

            @Test
            public void shouldUpdateTaskWithCorrectId() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getId()).isEqualByComparingTo(TASK_ID)
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldUpdateTaskWithCorrectTitle() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getTitle()).isEqualTo(maxLengthTitle)
                        ),
                        isA(LoggedInUser.class)
                );
            }

            @Test
            public void shouldUpdateTaskWithCorrectDescription() throws Exception {
                mockMvc.perform(put("/api/task/{taskId}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                );

                verify(crudService, times(1)).update(assertArg(
                            task -> assertThat(task.getDescription()).isEqualTo(maxLengthDescription)
                        ),
                        isA(LoggedInUser.class)
                );
            }
        }
    }
}


