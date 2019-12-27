package com.testwithspring.master.task

import com.testwithspring.master.user.PersonDTO
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

/**
 * The internal data model
 */

/**
 * Contains the user id of the assignee.
 */
data class Assignee(val id: Long)

/**
 * Contains the user id of the user who closed a task.
 */
data class Closer(val id: Long)

/**
 * Contains the user id of the user who created a task.
 */
data class Creator(val id: Long)

/**
 * Contains the user id of the user who modified a task.
 */
data class Modifier(val id: Long)

/**
 * Defines the resolutions of a task.
 */
enum class TaskResolution {
    DUPLICATE,
    DONE,
    WONT_DO
}

/**
 * Defines the statuses of a task.
 */
enum class TaskStatus {
    CLOSED,
    IN_PROGRESS,
    OPEN
}

/**
 * Contains the information of the created task.
 */
data class CreateTask(
        val creator: Creator,
        val description: String?,
        val status: TaskStatus,
        val title: String
)

/**
 * Contains the information of a single tag.
 */
data class TaskTag(val id: Long, val name: String)

/**
 * Contains the information of a single task.
 */
data class Task(val assignee: Assignee?,
                val closer: Closer?,
                val creator: Creator,
                val description: String,
                val id: Long,
                val modifier: Modifier,
                val resolution: TaskResolution?,
                val status: TaskStatus,
                val tags: List<TaskTag>,
                val title: String)

/**
 *  The public data model
 */

/**
 * Contains the information of the created task and
 * specifies the validation rules which ensure that
 * invalid data cannot be inserted into our database.
 */
data class CreateTaskDTO(
        @field: NotBlank
        @field: Size(max = 100)
        val title: String?,
        @field: Size(max = 500)
        val description: String?)

/**
 * Contains the information of a single tag.
 */
data class TaskTagDTO(val id: Long, val name: String)

/**
 * Contains the information of a single task.
 */
data class TaskDTO(val assignee: PersonDTO?,
                   val closer: PersonDTO?,
                   val creator: PersonDTO,
                   val description: String,
                   val id: Long,
                   val modifier: PersonDTO,
                   val resolution: TaskResolution?,
                   val status: TaskStatus,
                   val tags: List<TaskTagDTO>,
                   val title: String)

/**
 * Contains the information of a single task list item.
 */
data class TaskListItemDTO(val id: Long,
                           val title: String,
                           val status: TaskStatus)