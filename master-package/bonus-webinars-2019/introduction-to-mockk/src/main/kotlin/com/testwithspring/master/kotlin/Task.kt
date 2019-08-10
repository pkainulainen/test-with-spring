package com.testwithspring.master.kotlin

/**
 * Contains the user id of the user who created a task.
 */
data class Creator(val id: Long)

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
 * Contains the information of a single task.
 */
data class Task(val id: Long?,
                val creator: Creator,
                val title: String,
                val status: TaskStatus,
                val resolution: TaskResolution?)