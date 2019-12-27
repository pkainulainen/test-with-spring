package com.testwithspring.master.web

import com.testwithspring.master.task.CreateTaskDTO
import com.testwithspring.master.task.TaskCrudService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import com.testwithspring.master.task.TaskDTO
import com.testwithspring.master.task.TaskListItemDTO
import com.testwithspring.master.user.LoggedInUser
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

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
     * Creates a new task.
     * @param   input           The information of the created task.
     * @param   loggedInUser    The authenticated user.
     * @return  The information of the created task.
     */
    @RequestMapping(method = [RequestMethod.POST])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
            @RequestBody @Valid input: CreateTaskDTO,
            @AuthenticationPrincipal loggedInUser: LoggedInUser
    ): TaskDTO {
        LOGGER.info("User: #{} is creating a new task with information: {}",
                loggedInUser.id,
                input
        )

        val returned = service.create(input, loggedInUser)

        LOGGER.info("User: #{} created a new task with information: {}",
                loggedInUser.id,
                returned
        )

        return returned
    }

    /**
     * Finds the information of all tasks.
     * @return  A list that contains the found tasks. If no tasks
     *          are found, this function returns an empty list.
     */
    @RequestMapping(method = [RequestMethod.GET])
    fun findAll(): List<TaskListItemDTO> {
        LOGGER.info("Finding all tasks")

        val tasks = service.findAll()
        LOGGER.info("Found {} tasks")

        return tasks;
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