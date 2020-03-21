package com.testwithspring.master.task

import com.testwithspring.master.common.NotFoundException
import com.testwithspring.master.user.LoggedInUser
import com.testwithspring.master.user.PersonDTO
import com.testwithspring.master.user.PersonFinder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException

/**
 * Provides CRUD operations for tasks.
 */
@Service
open class TaskCrudService(@Autowired private val personFinder: PersonFinder,
                           @Autowired private val repository: TaskRepository) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TaskCrudService::class.java)
    }

    /**
     * Finds the information of all tasks.
     * @return  A list that contains the found tasks. If no tasks are
     *          found, this method returns an empty list.
     */
    @Transactional(readOnly = true)
    open fun findAll(): List<TaskListItemDTO> {
        LOGGER.info("Finding all tasks")

        val tasks = repository.findAll()
        LOGGER.info("Found {} tasks", tasks.size)

        return tasks;
    }

    /**
     * Creates a new task and inserts the information of the created
     * task into the database.
     * @param   input           The information of the created task.
     * @param   loggedInUser    The authenticated user.
     * @return  The information of the created task.
     */
    @Transactional
    open fun create(input: CreateTaskDTO, loggedInUser: LoggedInUser): TaskDTO {
        LOGGER.info("User: #{} is creating a new task by using the information: {}",
                loggedInUser.id,
                input
        )

        val newTask = CreateTask(
                creator = Creator(loggedInUser.id),
                title = input.title!!,
                description = input.description,
                status = TaskStatus.OPEN
        )

        val returned = repository.create(newTask);
        LOGGER.info("User: #{} created a new task with information: {}",
                loggedInUser.id,
                returned
        )

        return TaskDTO(
                assignee = null,
                closer = null,
                creator = personFinder.findPersonInformationByUserId(returned.creator.id),
                description = returned.description,
                id = returned.id,
                modifier = personFinder.findPersonInformationByUserId(returned.modifier.id),
                resolution = returned.resolution,
                status = returned.status,
                tags = listOf(),
                title = returned.title
        )
    }

    /**
     * Finds the information of the specified task.
     * @param   id  The id of the requested task.
     * @return  The information of the found task.
     * @throws com.testwithspring.master.common.NotFoundException   If no task is found with the given id.
     */
    @Transactional(readOnly = true)
    open fun findById(id: Long): TaskDTO {
        LOGGER.info("Finding the information of a task with id: #{}", id)

        val found = repository.findById(id) ?: throw NotFoundException("No task was found with id: #${id}")
        LOGGER.info("Found task: {}", found)

        return TaskDTO(
                assignee = findPersonInformation(found.assignee?.id),
                closer = findPersonInformation(found.closer?.id),
                creator = personFinder.findPersonInformationByUserId(found.creator.id),
                description = found.description,
                id = found.id,
                modifier = personFinder.findPersonInformationByUserId(found.modifier.id),
                resolution = found.resolution,
                status = found.status,
                tags = transformTagsToDTOs(found.tags),
                title = found.title
        )
    }

    private fun findPersonInformation(userId: Long?): PersonDTO? {
        return if (userId == null) {
            return null
        }
        else {
            personFinder.findPersonInformationByUserId(userId)
        }
    }

    private fun transformTagsToDTOs(models: List<TaskTag>): List<TaskTagDTO> {
        return models.map { TaskTagDTO(id = it.id, name = it.name) }
    }
}