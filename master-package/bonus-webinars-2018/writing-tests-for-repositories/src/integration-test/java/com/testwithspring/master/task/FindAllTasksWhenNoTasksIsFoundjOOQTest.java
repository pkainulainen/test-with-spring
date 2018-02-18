package com.testwithspring.master.task;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.testwithspring.master.TestTags;
import com.testwithspring.master.config.Profiles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JooqTest
@Import(TaskjOOQRepository.class)
@TestExecutionListeners(value =  DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@DatabaseSetup({
        "/com/testwithspring/master/users.xml",
        "/com/testwithspring/master/no-tasks-and-tags.xml"
})
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Tag(TestTags.INTEGRATION_TEST)
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
