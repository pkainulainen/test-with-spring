package com.testwithspring.master.web

import com.testwithspring.master.task.*
import com.testwithspring.master.user.PersonDTO
import com.testwithspring.master.user.PersonFinder
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Tag
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@Tag("unitTest")
@DisplayName("Perform CRUD operations for tasks")
class TaskCrudControllerTest {

    companion object {
        private const val ASSIGNEE_ID = 99L
        private const val ASSIGNEE_NAME = "Assignee"
        private const val CLOSER_ID = 55L
        private const val CLOSER_NAME = "Closer"
        private const val CREATOR_ID = 44L
        private const val CREATOR_NAME = "Creator"
        private const val DESCRIPTION = "This an example task"
        private const val MODIFIER_ID = 33L
        private const val MODIFIER_NAME = "Modifier"
        private const val LOGGED_IN_USER_ID = 5L
        private val RESOLUTION_DONE = TaskResolution.DONE
        private val STATUS_CLOSED = TaskStatus.CLOSED
        private const val TAG_ID = 1L
        private const val TAG_NAME = "tag"
        private const val TASK_ID = 1L
        private const val TITLE = "A task"
    }

    private lateinit var personFinder: PersonFinder
    private lateinit var repository: TaskRepository
    private lateinit var requestBuilder: TaskCrudRequestBuilder

    @BeforeEach
    fun configureSystemUnderTest() {
        personFinder = mockk()
        repository = mockk()
        val service = TaskCrudService(personFinder, repository)

        val mockMvc = MockMvcBuilders.standaloneSetup(TaskCrudController(service))
                .setControllerAdvice(TaskErrorHandler())
                .setCustomArgumentResolvers(AuthenticationHandlerMethodArgumentResolver(LOGGED_IN_USER_ID))
                .setLocaleResolver(WebTestConfig.fixedLocaleResolver())
                .setMessageConverters(WebTestConfig.objectMapperHttpMessageConverter())
                .build()
        requestBuilder = TaskCrudRequestBuilder(mockMvc)
    }

    @Nested
    @DisplayName("Create a new task")
    inner class Create {

        private val MAX_LENGTH_DESCRIPTION = 500
        private val MAX_LENGTH_TITLE = 100

        @Nested
        @DisplayName("When validation fails")
        inner class WhenValidationFails {

            private val ERROR_CODE_NOT_BLANK = "NotBlank"
            private val ERROR_CODE_SIZE = "Size"
            private val FIELD_NAME_TITLE = "title"

            @Nested
            @DisplayName("When the title and description are missing")
            inner class WhenTitleAndDescriptionAreMissing {

                private val INPUT = CreateTaskDTO(title = null, description = null)

                @Test
                @DisplayName("Should return the HTTP status code bad request")
                fun shouldReturnHttpStatusCodeBadRequest() {
                    requestBuilder.create(INPUT)
                            .andExpect(status().isBadRequest)
                }

                @Test
                @DisplayName("Should return validation errors as Json")
                fun shouldReturnValidationErrorsAsJson() {
                    requestBuilder.create(INPUT)
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                }

                @Test
                @DisplayName("Should return one validation error")
                fun shouldReturnOneValidationError() {
                    requestBuilder.create(INPUT)
                            .andExpect(jsonPath("$.fieldErrors", hasSize<Any>(1)))
                }

                @Test
                @DisplayName("Should return a validation error about missing title")
                fun shouldReturnValidationErrorAboutMissingTitle() {
                    requestBuilder.create(INPUT)
                            .andExpect(jsonPath("$.fieldErrors[0].field", equalTo(FIELD_NAME_TITLE)))
                            .andExpect(jsonPath("$.fieldErrors[0].errorCode", equalTo(ERROR_CODE_NOT_BLANK)))
                }

                @Test
                @DisplayName("Should not create a new task")
                fun shouldNotCreateNewTask() {
                    requestBuilder.create(INPUT)
                    verify(exactly = 0) { repository.create(any()) }
                }
            }

            @Nested
            @DisplayName("When the title and description are empty")
            inner class WhenTitleAndDescriptionAreEmpty {

                private val INPUT = CreateTaskDTO(title = "", description = "")

                @Test
                @DisplayName("Should return the HTTP status code bad request")
                fun shouldReturnHttpStatusCodeBadRequest() {
                    requestBuilder.create(INPUT)
                            .andExpect(status().isBadRequest)
                }

                @Test
                @DisplayName("Should return validation errors as Json")
                fun shouldReturnValidationErrorsAsJson() {
                    requestBuilder.create(INPUT)
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                }

                @Test
                @DisplayName("Should return one validation error")
                fun shouldReturnOneValidationError() {
                    requestBuilder.create(INPUT)
                            .andExpect(jsonPath("$.fieldErrors", hasSize<Any>(1)))
                }

                @Test
                @DisplayName("Should return a validation error about missing title")
                fun shouldReturnValidationErrorAboutMissingTitle() {
                    requestBuilder.create(INPUT)
                            .andExpect(jsonPath("$.fieldErrors[0].field", equalTo(FIELD_NAME_TITLE)))
                            .andExpect(jsonPath("$.fieldErrors[0].errorCode", equalTo(ERROR_CODE_NOT_BLANK)))
                }

                @Test
                @DisplayName("Should not create a new task")
                fun shouldNotCreateNewTask() {
                    requestBuilder.create(INPUT)
                    verify(exactly = 0) { repository.create(any()) }
                }
            }

            @Nested
            @DisplayName("When the title and description are too long")
            inner class WhenTitleAndDescriptionAreTooLong {

                private val INPUT = CreateTaskDTO(
                        title = TestStringBuilder.createStringWithLength(MAX_LENGTH_TITLE + 1),
                        description = TestStringBuilder.createStringWithLength(MAX_LENGTH_DESCRIPTION + 1)
                )

                @Test
                @DisplayName("Should return the HTTP status code bad request")
                fun shouldReturnHttpStatusCodeBadRequest() {
                    requestBuilder.create(INPUT)
                            .andExpect(status().isBadRequest)
                }

                @Test
                @DisplayName("Should return validation errors as Json")
                fun shouldReturnValidationErrorsAsJson() {
                    requestBuilder.create(INPUT)
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                }

                @Test
                @DisplayName("Should return two validation errors")
                fun shouldReturnOneValidationError() {
                    requestBuilder.create(INPUT)
                            .andExpect(jsonPath("$.fieldErrors", hasSize<Any>(2)))
                }

                @Test
                @DisplayName("Should return a validation error about too long title")
                fun shouldReturnValidationErrorAboutTooLongTitle() {
                    requestBuilder.create(INPUT)
                            .andExpect(jsonPath("$.fieldErrors[?(@.field == 'title')].errorCode", contains(ERROR_CODE_SIZE)))
                }

                @Test
                @DisplayName("Should return a validation error about too long description")
                fun shouldReturnValidationErrorAboutTooLongDescription() {
                    requestBuilder.create(INPUT)
                            .andExpect(jsonPath("$.fieldErrors[?(@.field == 'description')].errorCode", contains(ERROR_CODE_SIZE)))
                }

                @Test
                @DisplayName("Should not create a new task")
                fun shouldNotCreateNewTask() {
                    requestBuilder.create(INPUT)
                    verify(exactly = 0) { repository.create(any()) }
                }
            }
        }

        @Nested
        @DisplayName("When valid information is given")
        inner class WhenValidInformationIsGiven {

            private val LOGGED_IN_USER_NAME = "Jane Doe"
            private val MAX_DESCRIPTION = TestStringBuilder.createStringWithLength(MAX_LENGTH_DESCRIPTION)
            private val MAX_TITLE = TestStringBuilder.createStringWithLength(MAX_LENGTH_TITLE)
            private val STATUS_OPEN = TaskStatus.OPEN

            private val INPUT = CreateTaskDTO(title = MAX_TITLE, description = MAX_DESCRIPTION)

            @BeforeEach
            fun configureTestCases() {
                repositoryReturnsCreatedTask()
                personFinderReturnsPersonData()
            }

            private fun repositoryReturnsCreatedTask() {
                every { repository.create(any()) } returns Task(
                        assignee = null,
                        closer = null,
                        creator = Creator(LOGGED_IN_USER_ID),
                        description = MAX_DESCRIPTION,
                        id = TASK_ID,
                        modifier = Modifier(LOGGED_IN_USER_ID),
                        resolution = null,
                        status = STATUS_OPEN,
                        tags = listOf(),
                        title = MAX_TITLE
                )
            }

            private fun personFinderReturnsPersonData() {
                every { personFinder.findPersonInformationByUserId(LOGGED_IN_USER_ID) } returns
                        PersonDTO(name = LOGGED_IN_USER_NAME, userId = LOGGED_IN_USER_ID)
            }

            @Test
            @DisplayName("Should create a new task with the correct creator")
            fun shouldCreateNewTaskWithCorrectCreator() {
                requestBuilder.create(INPUT)
                verify { repository.create(match { it.creator.id == LOGGED_IN_USER_ID })}
            }

            @Test
            @DisplayName("Should create a new task with the correct description")
            fun shouldCreateNewTaskWithCorrectDescription() {
                requestBuilder.create(INPUT)
                verify { repository.create(match { it.description == MAX_DESCRIPTION })}
            }

            @Test
            @DisplayName("Should create a new task with the correct title")
            fun shouldCreateNewTaskWithCorrectTitle() {
                requestBuilder.create(INPUT)
                verify { repository.create(match { it.title == MAX_TITLE })}
            }

            @Test
            @DisplayName("Should create an open task")
            fun shouldCreateOpenTask() {
                requestBuilder.create(INPUT)
                verify { repository.create(match { it.status == STATUS_OPEN })}
            }

            @Test
            @DisplayName("Should return the HTTP status code created")
            fun shouldReturnHttpStatusCodeCreated() {
                requestBuilder.create(INPUT)
                        .andExpect(status().isCreated)
            }

            @Test
            @DisplayName("Should return the information of the created task as Json")
            fun shouldReturnInformationOfCreatedTaskAsJson() {
                requestBuilder.create(INPUT)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return the information of the created task")
            fun shouldReturnInformationOfCreatedTask() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.id", equalTo(TASK_ID.toInt())))
            }

            @Test
            @DisplayName("Should return a task that has no assignee")
            fun shouldReturnTaskThatHasNoAssignee() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.assignee", nullValue()))
            }

            @Test
            @DisplayName("Should return a task that has no closer")
            fun shouldReturnTaskThatHasNoCloser() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.closer", nullValue()))
            }

            @Test
            @DisplayName("Should return the the information of the person who created the task")
            fun shouldReturnCorrectCreator() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.creator.name", equalTo(LOGGED_IN_USER_NAME)))
                        .andExpect(jsonPath("$.creator.userId", equalTo(LOGGED_IN_USER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the the information of the person who modified the task")
            fun shouldReturnCorrectModifier() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.modifier.name", equalTo(LOGGED_IN_USER_NAME)))
                        .andExpect(jsonPath("$.modifier.userId", equalTo(LOGGED_IN_USER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the correct description and title")
            fun shouldReturnCorrectDescriptionAndTitle() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.description", equalTo(MAX_DESCRIPTION)))
                        .andExpect(jsonPath("$.title", equalTo(MAX_TITLE)))
            }

            @Test
            @DisplayName("Should return an open task")
            fun shouldReturnOpenTask() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.resolution", nullValue()))
                        .andExpect(jsonPath("$.status", equalTo(STATUS_OPEN.name)))
            }

            @Test
            @DisplayName("Should return a task that has no tags")
            fun shouldReturnTaskThatHasNoTags() {
                requestBuilder.create(INPUT)
                        .andExpect(jsonPath("$.tags", hasSize<Any>(0)))
            }
        }
    }

    @Nested
    @DisplayName("Find all tasks")
    inner class FindAll {

        @Nested
        @DisplayName("When no tasks are found")
        inner class WhenNoTasksAreFound {

            @BeforeEach
            fun repositoryReturnsEmptyList() {
                every { repository.findAll() } returns listOf()
            }

            @Test
            @DisplayName("Should return HTTP status code OK (200)")
            fun shouldReturnHttpStatusCodeOk() {
                requestBuilder.findAll()
                        .andExpect(status().isOk)
            }

            @Test
            @DisplayName("Should return the information of the found tasks as Json")
            fun shouldReturnFoundTasksAsJson() {
                requestBuilder.findAll()
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return an empty list")
            fun shouldReturnEmptyList() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$", hasSize<Any>(0)))
            }
        }

        @Nested
        @DisplayName("When one task is found")
        inner class WhenOneTaskIsFound {

            @BeforeEach
            fun repositoryReturnsOneTask() {
                every { repository.findAll() } returns listOf(TaskListItemDTO(
                        id = TASK_ID,
                        title = TITLE,
                        status = STATUS_CLOSED
                ))
            }

            @Test
            @DisplayName("Should return HTTP status code OK (200)")
            fun shouldReturnHttpStatusCodeOk() {
                requestBuilder.findAll()
                        .andExpect(status().isOk)
            }

            @Test
            @DisplayName("Should return the information of the found task as Json")
            fun shouldReturnFoundTasksAsJson() {
                requestBuilder.findAll()
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return one task")
            fun shouldReturnEmptyList() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$", hasSize<Any>(1)))
            }

            @Test
            @DisplayName("Should return the information of the found task")
            fun shouldReturnInformationOfFoundTask() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$[0].id", equalTo(TASK_ID.toInt())))
                        .andExpect(jsonPath("$[0].title", equalTo(TITLE)))
                        .andExpect(jsonPath("$[0].status", equalTo(STATUS_CLOSED.name)))
            }
        }
    }

    @Nested
    @DisplayName("Find a task by using its id as search criteria")
    inner class FindById {

        @Nested
        @DisplayName("When the requested task is not found")
        inner class WhenRequestedTaskIsNotFound {

            @BeforeEach
            fun repositoryReturnsNull() {
                every { repository.findById(TASK_ID) } returns null
            }

            @Test
            @DisplayName("Should return the HTTP status code not found (404)")
            fun shouldReturnHttpStatusCodeNotFound() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(status().isNotFound)
            }

            @Test
            @DisplayName("Should return an empty HTTP response")
            fun shouldReturnEmptyHttpResponse() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(content().string(""))
            }
        }

        @Nested
        @DisplayName("When the request task is found")
        inner class WhenRequestedTaskIsFound {

            @BeforeEach
            fun configureTestCases() {
                repositoryReturnsFoundTask()
                personFinderReturnsRelatedPersonData()
            }

            private fun repositoryReturnsFoundTask() {
                every { repository.findById(TASK_ID) } returns Task(
                        assignee = Assignee(ASSIGNEE_ID),
                        closer = Closer(CLOSER_ID),
                        creator = Creator(CREATOR_ID),
                        description = DESCRIPTION,
                        id = TASK_ID,
                        modifier = Modifier(MODIFIER_ID),
                        resolution = RESOLUTION_DONE,
                        status = STATUS_CLOSED,
                        tags = listOf(TaskTag(id = TAG_ID, name = TAG_NAME)),
                        title = TITLE
                )
            }

            private fun personFinderReturnsRelatedPersonData() {
                every { personFinder.findPersonInformationByUserId(ASSIGNEE_ID) } returns
                        PersonDTO(name = ASSIGNEE_NAME, userId = ASSIGNEE_ID)
                every { personFinder.findPersonInformationByUserId(CLOSER_ID) } returns
                        PersonDTO(name = CLOSER_NAME, userId = CLOSER_ID)
                every { personFinder.findPersonInformationByUserId(CREATOR_ID) } returns
                        PersonDTO(name = CREATOR_NAME, userId = CREATOR_ID)
                every { personFinder.findPersonInformationByUserId(MODIFIER_ID) } returns
                        PersonDTO(name = MODIFIER_NAME, userId = MODIFIER_ID)
            }

            @Test
            @DisplayName("Should return the HTTP status code OK (200)")
            fun shouldReturnHttpStatusCodeOk() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(status().isOk)
            }

            @Test
            @DisplayName("Should return the information of the found task as Json")
            fun shouldReturnInformationOfFoundTaskAsJson() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return the information of the found task")
            fun shouldReturnInformationOfFoundTask() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.id", equalTo(TASK_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the assignee")
            fun shouldReturnCorrectAssignee() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.assignee.name", equalTo(ASSIGNEE_NAME)))
                        .andExpect(jsonPath("$.assignee.userId", equalTo(ASSIGNEE_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the person who closed the task")
            fun shouldReturnCorrectCloser() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.closer.name", equalTo(CLOSER_NAME)))
                        .andExpect(jsonPath("$.closer.userId", equalTo(CLOSER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the the information of the person who created the task")
            fun shouldReturnCorrectCreator() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.creator.name", equalTo(CREATOR_NAME)))
                        .andExpect(jsonPath("$.creator.userId", equalTo(CREATOR_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the the information of the person who modified the task")
            fun shouldReturnCorrectModifier() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.modifier.name", equalTo(MODIFIER_NAME)))
                        .andExpect(jsonPath("$.modifier.userId", equalTo(MODIFIER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the correct description and title")
            fun shouldReturnCorrectDescriptionAndTitle() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.description", equalTo(DESCRIPTION)))
                        .andExpect(jsonPath("$.title", equalTo(TITLE)))
            }

            @Test
            @DisplayName("Should return a task that was closed because it was done")
            fun shouldReturnTaskThatIsDone() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.resolution", equalTo(RESOLUTION_DONE.name)))
                        .andExpect(jsonPath("$.status", equalTo(STATUS_CLOSED.name)))
            }

            @Test
            @DisplayName("Should return a task that has one tag")
            fun shouldReturnTaskThatHasOneTag() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.tags", hasSize<Any>(1)))
            }

            @Test
            @DisplayName("Should return the information of the found tag")
            fun shouldReturnInformationOfFoundTag() {
                requestBuilder.findById(TASK_ID)
                        .andExpect(jsonPath("$.tags[0].id", equalTo(TAG_ID.toInt())))
                        .andExpect(jsonPath("$.tags[0].name", equalTo(TAG_NAME)))
            }
        }
    }
}