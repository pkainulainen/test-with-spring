package com.testwithspring.master.task

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet


/**
 * This repository isn't meant to be used in product.
 * This repository exists only because it allows us to
 * compile our example application and write unit tests
 * for it.
 */
@Repository
open class TaskRepository(@Autowired private val jdbcTemplate: NamedParameterJdbcTemplate) {

    companion object {

        private val LOGGER: Logger = LoggerFactory.getLogger(TaskRepository::class.java)

        private const val QUERY_CREATE = "INSERT INTO tasks (" +
                "creator_id, " +
                "description, " +
                "modifier_id, " +
                "status, " +
                "title" +
                ") VALUES (" +
                ":creatorId, " +
                ":description, " +
                ":modifierId, " +
                ":status, " +
                ":title" +
                ")"

        private const val QUERY_FIND_ALL = "SELECT " +
                "t.id as id, " +
                "t.title as title, " +
                "t.status as status " +
                "FROM tasks t " +
                "ORDER BY t.id ASC"

        private const val QUERY_FIND_BY_ID = "SELECT " +
                "t.id as id, " +
                "t.assignee_id as assigneeId, " +
                "t.closer_id as closerId, " +
                "t.creator_id as creatorId, " +
                "t.description as description, " +
                "t.modifier_id as modifierId, " +
                "t.resolution as resolution, " +
                "t.status as status, " +
                "t.title as title, " +
                "ta.id as tags_id, " +
                "ta.name as tags_name " +
                "FROM tasks t " +
                "LEFT JOIN tasks_tags tt ON t.id = tt.task_id " +
                "LEFT JOIN tags ta ON ta.id = tt.tag_id " +
                "WHERE t.id = :id"

        private class TaskListItemRowMapper: RowMapper<TaskListItemDTO> {

            override fun mapRow(resultSet: ResultSet, rowNum: Int): TaskListItemDTO {
                return TaskListItemDTO(
                        id = resultSet.getLong("id"),
                        title = resultSet.getString("title"),
                        status = TaskStatus.valueOf(resultSet.getString("status"))
                )
            }
        }
    }

    private val rowMapper = JdbcTemplateMapperFactory.newInstance()
            .addKeys("id", "tags_id")
            .newRowMapper(Task::class.java)

    /**
     * Inserts a new task into the database.
     * @param   input   The information of the created task.
     * @return  The information of the created task.
     */
    @Transactional
    open fun create(input: CreateTask): Task {
        LOGGER.info("Creating a new task with information: {}", input)

        val keyHolder = GeneratedKeyHolder()
        val parameters = createInsertParameters(input)

        jdbcTemplate.update(QUERY_CREATE, parameters, keyHolder, arrayOf("id"))

        val created = Task(
                assignee = null,
                closer = null,
                creator = Creator(input.creator.id),
                description = input.description,
                id = keyHolder.key.toLong(),
                modifier = Modifier(input.creator.id),
                resolution = null,
                tags = listOf(),
                title = input.title,
                status = input.status
        )

        LOGGER.info("Created a new task with information: {}", created)
        return created
    }

    private fun createInsertParameters(input: CreateTask): SqlParameterSource {
        val parameters = MapSqlParameterSource()
        parameters.addValue("creatorId", input.creator.id)
        parameters.addValue("description", input.description)
        parameters.addValue("modifierId", input.creator.id)
        parameters.addValue("title", input.title)
        parameters.addValue("status", input.status.name)
        return parameters
    }

    /**
     * Finds the information of all tasks found from the database.
     * @return  A list of found tasks. If no tasks are found, this
     *          function returns an empty list.
     */
    @Transactional(readOnly = true)
    open fun findAll(): List<TaskListItemDTO> {
        LOGGER.info("Finding all tasks")

        val tasks = jdbcTemplate.query(QUERY_FIND_ALL, TaskListItemRowMapper())

        LOGGER.info("Found {} tasks", tasks.size)
        return tasks
    }

    /**
     * Finds the information of the specified task.
     * @param   id  The id of the requested task.
     * @return  This function returns {@code null}
     */
    @Transactional(readOnly = true)
    open fun findById(id: Long): Task? {
        LOGGER.info("Finding task by id: {}", id)

        val results = jdbcTemplate.query(
                QUERY_FIND_BY_ID,
                mapOf(Pair("id", id)),
                rowMapper
        )

        var found = null as Task?
        if (results.size == 1) {
            found = results[0]
        }
        else if (results.size > 1) {
            throw IncorrectResultSizeDataAccessException(1)
        }

        LOGGER.info("Fond task: {}", found);
        return found
    }
}