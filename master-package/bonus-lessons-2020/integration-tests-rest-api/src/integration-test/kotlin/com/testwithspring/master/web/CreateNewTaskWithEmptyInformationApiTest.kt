package com.testwithspring.master.web

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.annotation.ExpectedDatabase
import com.github.springtestdbunit.assertion.DatabaseAssertionMode
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.Tasks
import com.testwithspring.master.Users
import com.testwithspring.master.task.CreateTaskDTO
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebIntegrationTest
@DisplayName("Create a new task by using empty information")
class CreateNewTaskWithEmptyInformationApiTest (@Autowired private val mockMvc: MockMvc) {

    companion object {

        private const val ERROR_CODE_NOT_BLANK = "NotBlank"
        private const val FIELD_NAME_TITLE = "title"
    }

    private val requestBuilder = TaskCrudRequestBuilder(mockMvc)

    private val INPUT = CreateTaskDTO(
            description = "",
            title = ""
    )

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
                    .andExpect(status().isUnauthorized)
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
    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    @DisplayName("When the user is Anne Admin")
    inner class WhenUserIsAnneAdmin {


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
        @DisplayName("Should not insert a new task into the database")
        @ExpectedDatabase(value = "/com/testwithspring/master/no-tasks-and-tags.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
        fun shouldNotInsertNewTaskIntoDatabase() {
            requestBuilder.create(INPUT)
        }
    }
}