package com.testwithspring.master.web

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

/**
 * Creates and sends HTTP requests which are send to the
 * REST API which provides CRUD operations for task. The
 * goal of this class is to help us to remove duplicate
 * code from our unit and integration tests.
 */
class TaskCrudRequestBuilder(private val mockMvc: MockMvc) {

    /**
     * Creates and sends the HTTP request which obtains the
     * information of the specified task.
     * @param   id  The id of the requested task.
     * @return  A {@code ResultActions} object which allows us to write
     *          assertions for the returned HTTP response.
     */
    fun findById(id: Long): ResultActions {
        return mockMvc.perform(get("/api/task/{id}", id))
    }
}