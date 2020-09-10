package com.testwithspring.master.web

import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.Tasks
import com.testwithspring.master.Users
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
@DisplayName("Find all tasks")
class FindAllTasksApiTest(@Autowired private val mockMvc: MockMvc) {

    private val requestBuilder = TaskCrudRequestBuilder(mockMvc)

    @Nested
    @WebIntegrationTest
    @DisplayName("When no tasks are found from the database")
    inner class WhenNoTasksAreFound {

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
            fun shouldReturnHttpStatusCodeUnauthorizedForAnonymousUser() {
                requestBuilder.findAll()
                        .andExpect(status().isUnauthorized)
            }

            @Test
            @DisplayName("Should return an empty HTTP response")
            fun shouldReturnEmptyHttpResponse() {
                requestBuilder.findAll()
                        .andExpect(content().string(""))
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
            @DisplayName("Should return the found tasks as Json")
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
            @DisplayName("Should return the found tasks as Json")
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
    }

    @Nested
    @WebIntegrationTest
    @DisplayName("When two tasks are found from the database")
    inner class WhenTwoTasksAreFound {

        @Nested
        @WebIntegrationTest
        @DatabaseSetup(value = [
            "/com/testwithspring/master/users.xml",
            "/com/testwithspring/master/tasks.xml"
        ])
        @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
        @DisplayName("When the user is anonymous")
        inner class WhenUserIsAnonymous {

            @Test
            @DisplayName("Should return the HTTP status code unauthorized")
            fun shouldReturnHttpStatusCodeUnauthorizedForAnonymousUser() {
                requestBuilder.findAll()
                        .andExpect(status().isUnauthorized)
            }

            @Test
            @DisplayName("Should return an empty HTTP response")
            fun shouldReturnEmptyHttpResponse() {
                requestBuilder.findAll()
                        .andExpect(content().string(""))
            }
        }

        @Nested
        @WebIntegrationTest
        @DatabaseSetup(value = [
            "/com/testwithspring/master/users.xml",
            "/com/testwithspring/master/tasks.xml"
        ])
        @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
        @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
        @DisplayName("When the user is John Doe")
        inner class WhenUserIsJohnDoe {

            @Test
            @DisplayName("Should return the found tasks as Json")
            fun shouldReturnFoundTasksAsJson() {
                requestBuilder.findAll()
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return two tasks")
            fun shouldReturnTwoTasks() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$", hasSize<Any>(2)))
            }

            @Test
            @DisplayName("Should return the oldest task as the first task of the returned list")
            fun shouldReturnOldestTaskAsFirstTaskOfReturnedList() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$[0].id", equalTo(Tasks.WriteExampleApplication.ID.toInt())))
                        .andExpect(jsonPath("$[0].title", equalTo(Tasks.WriteExampleApplication.TITLE)))
                        .andExpect(jsonPath("$[0].status", equalTo(Tasks.WriteExampleApplication.STATUS.name)))
            }

            @Test
            @DisplayName("Should return the newest task as the second task of the returned list")
            fun shouldReturnNewestTaskAsSecondTaskOfReturnedList() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$[1].id", equalTo(Tasks.WriteLesson.ID.toInt())))
                        .andExpect(jsonPath("$[1].title", equalTo(Tasks.WriteLesson.TITLE)))
                        .andExpect(jsonPath("$[1].status", equalTo(Tasks.WriteLesson.STATUS.name)))
            }
        }

        @Nested
        @WebIntegrationTest
        @DatabaseSetup(value = [
            "/com/testwithspring/master/users.xml",
            "/com/testwithspring/master/tasks.xml"
        ])
        @DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader::class)
        @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
        @DisplayName("When the user is Anne Admin")
        inner class WhenUserIsAnneAdmin {

            @Test
            @DisplayName("Should return the found tasks as Json")
            fun shouldReturnFoundTasksAsJson() {
                requestBuilder.findAll()
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return two tasks")
            fun shouldReturnTwoTasks() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$", hasSize<Any>(2)))
            }

            @Test
            @DisplayName("Should return the oldest task as the first task of the returned list")
            fun shouldReturnOldestTaskAsFirstTaskOfReturnedList() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$[0].id", equalTo(Tasks.WriteExampleApplication.ID.toInt())))
                        .andExpect(jsonPath("$[0].title", equalTo(Tasks.WriteExampleApplication.TITLE)))
                        .andExpect(jsonPath("$[0].status", equalTo(Tasks.WriteExampleApplication.STATUS.name)))
            }

            @Test
            @DisplayName("Should return the newest task as the second task of the returned list")
            fun shouldReturnNewestTaskAsSecondTaskOfReturnedList() {
                requestBuilder.findAll()
                        .andExpect(jsonPath("$[1].id", equalTo(Tasks.WriteLesson.ID.toInt())))
                        .andExpect(jsonPath("$[1].title", equalTo(Tasks.WriteLesson.TITLE)))
                        .andExpect(jsonPath("$[1].status", equalTo(Tasks.WriteLesson.STATUS.name)))
            }
        }
    }
}