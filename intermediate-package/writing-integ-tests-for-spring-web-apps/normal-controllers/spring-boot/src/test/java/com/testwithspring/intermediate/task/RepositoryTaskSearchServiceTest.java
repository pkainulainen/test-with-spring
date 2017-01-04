package com.testwithspring.intermediate.task;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import static com.testwithspring.intermediate.TestDoubles.stub;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class RepositoryTaskSearchServiceTest {

    private TaskRepository repository;
    private RepositoryTaskSearchService service;

    @Before
    public void configureSystemUnderTest() {
        repository = stub(TaskRepository.class);
        service = new RepositoryTaskSearchService(repository);
    }

    public class Search {

        private final Long TASK_ID = 1L;
        private final String TITLE = "Write an example test";
        private final String SEARCH_TERM = "examp";
        private final TaskStatus STATUS = TaskStatus.OPEN;

        @Before
        public void returnOneTask() {
            TaskListDTO task = createTask();
            given(repository.search(SEARCH_TERM)).willReturn(Collections.singletonList(task));
        }

        private TaskListDTO createTask() {
            TaskListDTO task = new TaskListDTO();

            task.setId(TASK_ID);
            task.setStatus(STATUS);
            task.setTitle(TITLE);

            return task;
        }

        @Test
        public void shouldReturnOneTask() {
            List<TaskListDTO> tasks = service.search(SEARCH_TERM);
            assertThat(tasks).hasSize(1);
        }

        @Test
        public void shouldReturnTaskWithCorrectInformation() {
            TaskListDTO task = service.search(SEARCH_TERM).get(0);

            assertThat(task.getId()).isEqualByComparingTo(TASK_ID);
            assertThat(task.getStatus()).isEqualTo(STATUS);
            assertThat(task.getTitle()).isEqualTo(TITLE);
        }
    }
}
