package com.testwithspring.master.kotlin

interface TaskRepository {

    fun findById(id: Long): Task?

    fun findByCreatorAndStatus(creatorId: Long, status: TaskStatus): List<Task>

    fun create(task: Task): Task
}