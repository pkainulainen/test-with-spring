package com.testwithspring.master.web

import com.testwithspring.master.task.TaskCrudService
import com.testwithspring.master.task.TaskRepository
import com.testwithspring.master.user.PersonFinder
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@Tag("unitTest")
@DisplayName("Perform CRUD operations for tasks")
class TaskCrudControllerTest {

    companion object {
        private const val TASK_ID = 1L
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
    }
}