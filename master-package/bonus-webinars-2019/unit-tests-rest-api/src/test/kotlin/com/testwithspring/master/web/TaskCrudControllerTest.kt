package com.testwithspring.master.web

import com.testwithspring.master.task.*
import com.testwithspring.master.user.PersonDTO
import com.testwithspring.master.user.PersonFinder
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
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
        private val RESOLUTION_DONE = TaskResolution.DONE
        private val STATUS_CLOSED = TaskStatus.CLOSED
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
                .setLocaleResolver(WebTestConfig.fixedLocaleResolver())
                .setMessageConverters(WebTestConfig.objectMapperHttpMessageConverter())
                .build()
        requestBuilder = TaskCrudRequestBuilder(mockMvc)
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
                        title = TITLE
                )
            }

            private fun personFinderReturnsRelatedPersonData() {
                every { personFinder.findPersonInformationByUserId(ASSIGNEE_ID) } returns PersonDTO(ASSIGNEE_NAME, ASSIGNEE_ID)
                every { personFinder.findPersonInformationByUserId(CLOSER_ID) } returns PersonDTO(CLOSER_NAME, CLOSER_ID)
                every { personFinder.findPersonInformationByUserId(CREATOR_ID) } returns PersonDTO(CREATOR_NAME, CREATOR_ID)
                every { personFinder.findPersonInformationByUserId(MODIFIER_ID) } returns PersonDTO(MODIFIER_NAME, MODIFIER_ID)
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
        }
    }
}