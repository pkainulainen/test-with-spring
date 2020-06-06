package com.testwithspring.master.web

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebIntegrationTest
class FindAllTasksTest(@Autowired private val mockMvc: MockMvc) {

    private val requestBuilder = TaskCrudRequestBuilder(mockMvc)

    @Test
    @DisplayName("Should return the HTTP status code unauthorized for anonymous user")
    fun shouldReturnHttpStatusCodeUnauthorizedForAnonymousUser() {
        requestBuilder.findAll()
                .andExpect(status().isUnauthorized)
    }
}