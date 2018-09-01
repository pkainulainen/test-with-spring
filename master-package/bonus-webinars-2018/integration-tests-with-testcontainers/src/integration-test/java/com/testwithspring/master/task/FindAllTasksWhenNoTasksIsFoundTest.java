package com.testwithspring.master.task;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.testwithspring.master.RepositoryIntegrationTest;
import com.testwithspring.master.IntegrationTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryIntegrationTest(classes = IntegrationTestContext.class)
@DatabaseSetup({
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/no-tasks-and-tags.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@DisplayName("Find all tasks from the database when no tasks is found")
class FindAllTasksWhenNoTasksIsFoundTest {

    @Autowired
    private TaskRepository repository;

    @Test
    @DisplayName("Should return zero tasks")
    void shouldReturnTwoTasks() {
        List<TaskListDTO> tasks = repository.findAll();
        assertThat(tasks).isEmpty();
    }
}
