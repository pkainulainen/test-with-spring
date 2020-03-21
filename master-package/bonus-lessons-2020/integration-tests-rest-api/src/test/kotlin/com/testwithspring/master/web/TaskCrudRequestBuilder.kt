package com.testwithspring.master.web

import com.testwithspring.master.task.CreateTaskDTO
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

/**
 * Creates and sends HTTP requests which are send to the
 * REST API which provides CRUD operations for task. The
 * goal of this class is to help us to remove duplicate
 * code from our unit and integration tests.
 */
class TaskCrudRequestBuilder(private val mockMvc: MockMvc) {

    private val objectMapper = WebTestConfig.objectMapper()

    /**
     * Creates and sends the HTTP request which creates
     * a new task.
     * @return  A {@code ResultActions} object which allows us to write
     *          assertions for the returned HTTP response.
     */
    fun create(input: CreateTaskDTO): ResultActions {
        return mockMvc.perform(post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(input))
        )
    }

    /**
     * Creates and sends the HTTP request which obtains
     * the information of all tasks.
     * @return  A {@code ResultActions} object which allows us to write
     *          assertions for the returned HTTP response.
     */
    fun findAll(): ResultActions {
        return mockMvc.perform(get("/api/task"))
    }

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