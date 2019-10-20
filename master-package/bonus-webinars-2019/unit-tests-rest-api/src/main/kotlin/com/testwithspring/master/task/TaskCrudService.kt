package com.testwithspring.master.task

import com.testwithspring.master.common.NotFoundException
import com.testwithspring.master.user.PersonDTO
import com.testwithspring.master.user.PersonFinder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
}