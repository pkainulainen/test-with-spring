package com.testwithspring.master.task;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.testwithspring.master.DbUnitJooqTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DbUnitJooqTest
@Import(TaskjOOQRepository.class)
@DatabaseSetup({
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/no-tasks-and-tags.xml"
})
@DisplayName("Find all tasks from the database when no tasks is found")
class FindAllTasksWhenNoTasksIsFoundjOOQTest {

    @Autowired
    private TaskjOOQRepository repository;

    @Test
    @DisplayName("Should return zero tasks")
    void shouldReturnTwoTasks() {
        List<TaskListDTO> tasks = repository.findAll();
        assertThat(tasks).isEmpty();
    }
}
