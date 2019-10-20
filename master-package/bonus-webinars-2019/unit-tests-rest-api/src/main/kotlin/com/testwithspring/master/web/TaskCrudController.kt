package com.testwithspring.master.web

import com.testwithspring.master.task.TaskCrudService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.testwithspring.master.task.TaskDTO
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Implements a REST API which provides CRUD operations
 * for tasks.
 */
@RestController
@RequestMapping("/api/task")
open class TaskCrudController(@Autowired private val service: TaskCrudService) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TaskCrudController::class.java)
    }

    /**
     * Finds the information of the specified task.
     * @param id    The id of the requested task.
     * @return      The information of the found task.
     * @throws com.testwithspring.master.common.NotFoundException if no task is found with the given id
     */
    @RequestMapping(value = ["{id}"], method = [RequestMethod.GET])
    fun findById(@PathVariable("id") id: Long): TaskDTO {
        LOGGER.info("Finding task with id: {}", id)

        val found = service.findById(id)
        LOGGER.info("Found task: {}", found)

        return found
    }
}