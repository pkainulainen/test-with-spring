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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepositoryImpl implements CustomTaskRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRepositoryImpl.class);

    private static final String QUERY_FIND_ALL = "SELECT id, status, title FROM tasks";
    private static final String QUERY_SEARCH = "SELECT id, status, title " +
            "FROM tasks t " +
            "WHERE " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
            "ORDER BY t.title ASC";

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

    @Transactional(readOnly = true)
    @Override
    public List<TaskListDTO> search(String searchTerm) {
        LOGGER.info("Finding tasks by using search term: {}", searchTerm);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("searchTerm", searchTerm);

        List<TaskListDTO> results = jdbcTemplate.query(QUERY_SEARCH, queryParams, new TaskListRowMapper());
        LOGGER.info("Found {} tasks by using search term: {}", results.size(), searchTerm);

        return results;
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
