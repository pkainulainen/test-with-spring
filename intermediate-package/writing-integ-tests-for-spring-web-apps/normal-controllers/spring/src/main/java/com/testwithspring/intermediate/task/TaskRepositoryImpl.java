package com.testwithspring.intermediate.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskRepositoryImpl implements CustomTaskRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    private static final String QUERY_FIND_ALL = "SELECT id, status, title FROM tasks";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    TaskRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskListDTO> findAll() {
        LOGGER.info("Finding all tasks");

        List<TaskListDTO> tasks = jdbcTemplate.query(QUERY_FIND_ALL, new TaskListRowMapper());
        LOGGER.info("Found {} tasks", tasks.size());

        return tasks;
    }

    private class TaskListRowMapper implements RowMapper<TaskListDTO> {

        @Override
        public TaskListDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            TaskListDTO task = new TaskListDTO();

            task.setId(rs.getLong("id"));
            task.setStatus(TaskStatus.valueOf(rs.getString("status")));
            task.setTitle(rs.getString("title"));

            return task;
        }
    }
}
