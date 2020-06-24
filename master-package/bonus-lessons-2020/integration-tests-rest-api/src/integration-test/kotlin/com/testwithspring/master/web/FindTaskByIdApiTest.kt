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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebIntegrationTest
@DisplayName("Find task by id")
class FindTaskByIdApiTest(@Autowired private val mockMvc: MockMvc) {

    private val requestBuilder = TaskCrudRequestBuilder(mockMvc)

    @Nested
    @DisplayName("When the requested task isn't found")
    inner class WhenRequestedTaskIsNotFound {

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
                requestBuilder.findById(Tasks.ID_NOT_FOUND)
                        .andExpect(status().isUnauthorized)
            }

            @Test
            @DisplayName("Should return an empty HTTP response")
            fun shouldReturnEmptyHttpResponse() {
                requestBuilder.findById(Tasks.ID_NOT_FOUND)
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
            @DisplayName("Should return the HTTP status code not found")
            fun shouldReturnHttpStatusCodeNotFound() {
                requestBuilder.findById(Tasks.ID_NOT_FOUND)
                        .andExpect(status().isNotFound)
            }

            @Test
            @DisplayName("Should return an empty HTTP response")
            fun shouldReturnEmptyHttpResponse() {
                requestBuilder.findById(Tasks.ID_NOT_FOUND)
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
        @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
        @DisplayName("When the user is Anne Admin")
        inner class WhenUserIsAnneAdmin {

            @Test
            @DisplayName("Should return the HTTP status code not found")
            fun shouldReturnHttpStatusCodeNotFound() {
                requestBuilder.findById(Tasks.ID_NOT_FOUND)
                        .andExpect(status().isNotFound)
            }

            @Test
            @DisplayName("Should return an empty HTTP response")
            fun shouldReturnEmptyHttpResponse() {
                requestBuilder.findById(Tasks.ID_NOT_FOUND)
                        .andExpect(content().string(""))
            }
        }
    }

    @Nested
    @DisplayName("When the requested task is found")
    inner class WhenRequestedTaskIsFound {

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
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(status().isUnauthorized)
            }

            @Test
            @DisplayName("Should return an empty HTTP response")
            fun shouldReturnEmptyHttpResponse() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
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
            @DisplayName("Should return the HTTP status code ok")
            fun shouldReturnHttpStatusCodeOk() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(status().isOk)
            }

            @Test
            @DisplayName("Should return the information of the found task as Json")
            fun shouldReturnInformationOfFoundTaskAsJson() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return the information of the found task")
            fun shouldReturnInformationOfFoundTask() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.id", equalTo(Tasks.WriteExampleApplication.ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the assignee")
            fun shouldReturnInformationOfAssignee() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.assignee.name", equalTo(Tasks.WriteExampleApplication.ASSIGNEE_NAME)))
                        .andExpect(jsonPath("$.assignee.userId", equalTo(Tasks.WriteExampleApplication.ASSIGNEE_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the person who closed the task")
            fun shouldReturnInformationOfCloser() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.closer.name", equalTo(Tasks.WriteExampleApplication.CLOSER_NAME)))
                        .andExpect(jsonPath("$.closer.userId", equalTo(Tasks.WriteExampleApplication.CLOSER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the person who created the task")
            fun shouldReturnInformationOfCreator() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.creator.name", equalTo(Tasks.WriteExampleApplication.CREATOR_NAME)))
                        .andExpect(jsonPath("$.creator.userId", equalTo(Tasks.WriteExampleApplication.CREATOR_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the person who modifier the task")
            fun shouldReturnInformationOfModifier() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.modifier.name", equalTo(Tasks.WriteExampleApplication.MODIFIER_NAME)))
                        .andExpect(jsonPath("$.modifier.userId", equalTo(Tasks.WriteExampleApplication.MODIFIER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the correct description and title")
            fun shouldReturnCorrectDescriptionAndTitle() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.description", equalTo(Tasks.WriteExampleApplication.DESCRIPTION)))
                        .andExpect(jsonPath("$.title", equalTo(Tasks.WriteExampleApplication.TITLE)))
            }

            @Test
            @DisplayName("Should return a task that was closed because it was done")
            fun shouldReturnTaskThatIsDone() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.resolution", equalTo(Tasks.WriteExampleApplication.RESOLUTION.name)))
                        .andExpect(jsonPath("$.status", equalTo(Tasks.WriteExampleApplication.STATUS.name)))
            }

            @Test
            @DisplayName("Should return a task that has one tag")
            fun shouldReturnTaskThatHasOneTag() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.tags", hasSize<Any>(1)))
            }

            @Test
            @DisplayName("Should return the information of the found tag")
            fun shouldReturnInformationOfFoundTag() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.tags[0].id", equalTo(Tasks.WriteExampleApplication.Tags.Example.ID.toInt())))
                        .andExpect(jsonPath("$.tags[0].name", equalTo(Tasks.WriteExampleApplication.Tags.Example.NAME)))
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
            @DisplayName("Should return the HTTP status code ok")
            fun shouldReturnHttpStatusCodeOk() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(status().isOk)
            }

            @Test
            @DisplayName("Should return the information of the found task as Json")
            fun shouldReturnInformationOfFoundTaskAsJson() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            }

            @Test
            @DisplayName("Should return the information of the found task")
            fun shouldReturnInformationOfFoundTask() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.id", equalTo(Tasks.WriteExampleApplication.ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the assignee")
            fun shouldReturnInformationOfAssignee() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.assignee.name", equalTo(Tasks.WriteExampleApplication.ASSIGNEE_NAME)))
                        .andExpect(jsonPath("$.assignee.userId", equalTo(Tasks.WriteExampleApplication.ASSIGNEE_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the person who closed the task")
            fun shouldReturnInformationOfCloser() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.closer.name", equalTo(Tasks.WriteExampleApplication.CLOSER_NAME)))
                        .andExpect(jsonPath("$.closer.userId", equalTo(Tasks.WriteExampleApplication.CLOSER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the person who created the task")
            fun shouldReturnInformationOfCreator() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.creator.name", equalTo(Tasks.WriteExampleApplication.CREATOR_NAME)))
                        .andExpect(jsonPath("$.creator.userId", equalTo(Tasks.WriteExampleApplication.CREATOR_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the information of the person who modifier the task")
            fun shouldReturnInformationOfModifier() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.modifier.name", equalTo(Tasks.WriteExampleApplication.MODIFIER_NAME)))
                        .andExpect(jsonPath("$.modifier.userId", equalTo(Tasks.WriteExampleApplication.MODIFIER_ID.toInt())))
            }

            @Test
            @DisplayName("Should return the correct description and title")
            fun shouldReturnCorrectDescriptionAndTitle() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.description", equalTo(Tasks.WriteExampleApplication.DESCRIPTION)))
                        .andExpect(jsonPath("$.title", equalTo(Tasks.WriteExampleApplication.TITLE)))
            }

            @Test
            @DisplayName("Should return a task that was closed because it was done")
            fun shouldReturnTaskThatIsDone() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.resolution", equalTo(Tasks.WriteExampleApplication.RESOLUTION.name)))
                        .andExpect(jsonPath("$.status", equalTo(Tasks.WriteExampleApplication.STATUS.name)))
            }

            @Test
            @DisplayName("Should return a task that has one tag")
            fun shouldReturnTaskThatHasOneTag() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.tags", hasSize<Any>(1)))
            }

            @Test
            @DisplayName("Should return the information of the found tag")
            fun shouldReturnInformationOfFoundTag() {
                requestBuilder.findById(Tasks.WriteExampleApplication.ID)
                        .andExpect(jsonPath("$.tags[0].id", equalTo(Tasks.WriteExampleApplication.Tags.Example.ID.toInt())))
                        .andExpect(jsonPath("$.tags[0].name", equalTo(Tasks.WriteExampleApplication.Tags.Example.NAME)))
            }
        }
    }
}