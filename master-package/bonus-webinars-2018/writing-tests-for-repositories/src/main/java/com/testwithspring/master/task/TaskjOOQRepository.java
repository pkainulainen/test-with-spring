package com.testwithspring.master.task;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
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
class TaskjOOQRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskjOOQRepository.class);

    private final DSLContext jooq;

    @Autowired
    TaskjOOQRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    @Transactional(readOnly = true)
    List<TaskListDTO> findAll() {
        LOGGER.info("Finding all tasks");

        List<TaskListDTO> tasks = jooq.select(
                DSL.field("id"),
                DSL.field("status"),
                DSL.field("title")
        )
                .from("tasks")
                .orderBy(DSL.field("id").asc())
                .fetchInto(TaskListDTO.class);

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
