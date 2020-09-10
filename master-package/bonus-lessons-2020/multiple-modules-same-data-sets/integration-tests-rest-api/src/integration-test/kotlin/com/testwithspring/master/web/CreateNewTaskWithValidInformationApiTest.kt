package com.testwithspring.master.web

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.IdColumnReset
import com.testwithspring.master.Tasks
import com.testwithspring.master.Users
import com.testwithspring.master.task.CreateTaskDTO
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebIntegrationTest
@DisplayName("Create a new task by using valid information")
class CreateNewTaskWithValidInformationApiTest (
        @Autowired private val jdbcTemplate: NamedParameterJdbcTemplate,
        @Autowired private val mockMvc: MockMvc
) {

    companion object {

        private val NEXT_FREE_ID = 1L
    }

    private val idColumnReset = IdColumnReset(jdbcTemplate)

    private val requestBuilder = TaskCrudRequestBuilder(mockMvc)

    private val INPUT = CreateTaskDTO(
            description = Tasks.WriteLesson.DESCRIPTION,
            title = Tasks.WriteLesson.TITLE
    )

    @BeforeEach
    fun resetIdColumn() {
        idColumnReset.resetIdColumns("tasks")
    }

    @Nested
    @WebIntegrationTest
    @DatabaseSetup(value = [
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/no-tasks-and-tags.xml"
    ])
    @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
    @DisplayName("When the user is anonymous")
    inner class WhenUserIsAnonymous {

        @Test
        @DisplayName("Should return the HTTP status code unauthorized")
        fun shouldReturnHttpStatusCodeUnauthorized() {
            requestBuilder.create(INPUT)
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized)
        }

        @Test
        @DisplayName("Should return an empty HTTP response")
        fun shouldReturnEmptyHttpResponse() {
            requestBuilder.create(INPUT)
                    .andExpect(content().string(""))
        }

        @Test
        @DisplayName("Should not insert a new task into the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/no-tasks-and-tags.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldNotInsertNewTaskIntoDatabase() {
            requestBuilder.create(INPUT)
        }
    }

    @Nested
    @WebIntegrationTest
    @DatabaseSetup(value = [
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/no-tasks-and-tags.xml"
    ])
    @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    @DisplayName("When the user is John Doe")
    inner class WhenUserIsJohnDoe {

        @Test
        @DisplayName("Should use the next free id when a new task is saved to the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-id-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldUseNextFreeIdWhenNewTaskIsSavedToDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should insert correct lifecycle column values into the database")
        @ExpectedDatabase(value = "create-task-lifecycle-columns-johndoe-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldInsertCorrectLifecycleColumnValuesIntoDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should save the correct status and resolution to the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-status-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldSaveCorrectStatusAndResolutionToDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should insert the correct title and description to the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-title-description-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldInsertCorrectTitleAndDescriptionToDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should create a task that has no tags")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-tags-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldCreateTaskThatHasNoTags() {
            requestBuilder.create(INPUT)
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
        @DisplayName("Should return the id of the created task")
        fun shouldReturnInformationOfCreatedTask() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.id", equalTo(NEXT_FREE_ID.toInt())))
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
                    .andExpect(jsonPath("$.creator.name", equalTo(Users.JohnDoe.NAME)))
                    .andExpect(jsonPath("$.creator.userId", equalTo(Users.JohnDoe.ID.toInt())))
        }

        @Test
        @DisplayName("Should return the the information of the person who modified the task")
        fun shouldReturnCorrectModifier() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.modifier.name", equalTo(Users.JohnDoe.NAME)))
                    .andExpect(jsonPath("$.modifier.userId", equalTo(Users.JohnDoe.ID.toInt())))
        }

        @Test
        @DisplayName("Should return the correct description and title")
        fun shouldReturnCorrectDescriptionAndTitle() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.description", equalTo(Tasks.WriteLesson.DESCRIPTION)))
                    .andExpect(jsonPath("$.title", equalTo(Tasks.WriteLesson.TITLE)))
        }

        @Test
        @DisplayName("Should return an open task")
        fun shouldReturnOpenTask() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.resolution", nullValue()))
                    .andExpect(jsonPath("$.status", equalTo(Tasks.WriteLesson.STATUS.name)))
        }

        @Test
        @DisplayName("Should return a task that has no tags")
        fun shouldReturnTaskThatHasNoTags() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.tags", hasSize<Any>(0)))
        }
    }

    @Nested
    @WebIntegrationTest
    @DatabaseSetup(value = [
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/no-tasks-and-tags.xml"
    ])
    @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @DisplayName("When the user is Anne Admin")
    inner class WhenUserIsAnneAdmin {

        @Test
        @DisplayName("Should use the next free id when a new task is saved to the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-id-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldUseNextFreeIdWhenNewTaskIsSavedToDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should insert correct lifecycle column values into the database")
        @ExpectedDatabase(value = "create-task-lifecycle-columns-anneadmin-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldInsertCorrectLifecycleColumnValuesIntoDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should save the correct status and resolution to the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-status-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldSaveCorrectStatusAndResolutionToDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should insert the correct title and description to the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-title-description-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldInsertCorrectTitleAndDescriptionToDatabase() {
            requestBuilder.create(INPUT)
        }

        @Test
        @DisplayName("Should create a task that has no tags")
        @ExpectedDatabase(value = "/com/testwithspring/master/task/create-task-tags-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldCreateTaskThatHasNoTags() {
            requestBuilder.create(INPUT)
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
        @DisplayName("Should return the id of the created task")
        fun shouldReturnInformationOfCreatedTask() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.id", equalTo(NEXT_FREE_ID.toInt())))
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
                    .andExpect(jsonPath("$.creator.name", equalTo(Users.AnneAdmin.NAME)))
                    .andExpect(jsonPath("$.creator.userId", equalTo(Users.AnneAdmin.ID.toInt())))
        }

        @Test
        @DisplayName("Should return the the information of the person who modified the task")
        fun shouldReturnCorrectModifier() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.modifier.name", equalTo(Users.AnneAdmin.NAME)))
                    .andExpect(jsonPath("$.modifier.userId", equalTo(Users.AnneAdmin.ID.toInt())))
        }

        @Test
        @DisplayName("Should return the correct description and title")
        fun shouldReturnCorrectDescriptionAndTitle() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.description", equalTo(Tasks.WriteLesson.DESCRIPTION)))
                    .andExpect(jsonPath("$.title", equalTo(Tasks.WriteLesson.TITLE)))
        }

        @Test
        @DisplayName("Should return an open task")
        fun shouldReturnOpenTask() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.resolution", nullValue()))
                    .andExpect(jsonPath("$.status", equalTo(Tasks.WriteLesson.STATUS.name)))
        }

        @Test
        @DisplayName("Should return a task that has no tags")
        fun shouldReturnTaskThatHasNoTags() {
            requestBuilder.create(INPUT)
                    .andExpect(jsonPath("$.tags", hasSize<Any>(0)))
        }
    }
}