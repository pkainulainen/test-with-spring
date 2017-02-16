package com.testwithspring.intermediate.web;

import com.testwithspring.intermediate.UnitTest;
import com.testwithspring.intermediate.task.TaskListDTO;
import com.testwithspring.intermediate.task.TaskSearchService;
import com.testwithspring.intermediate.task.TaskStatus;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static com.testwithspring.intermediate.TestDoubles.stub;
import static com.testwithspring.intermediate.web.WebTestConfig.objectMapperHttpMessageConverter;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class TaskSearchControllerTest {

    private TaskSearchService service;
    private MockMvc mockMvc;

    /**
     * You need to configure only the view resolver because it is the only component
     * of the Spring MVC infrastructure that is relevant for the test cases found
     * from this test class.
     */
    @Before
    public void configureTheSystemUnderTest() {
        service = stub(TaskSearchService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(new TaskSearchController(service))
                .setMessageConverters(objectMapperHttpMessageConverter())
                .build();
    }

    public class ShowSearchResults {

        private final String REQUEST_PARAMETER_SEARCH_TERM = "searchTerm";
        private final String SEARCH_TERM = "searchTerm";

        /**
         * These two test methods are added into this test class because both of them
         * test behavior that should happen in every case (tasks are not found and
         * tasks are not found).
         *
         * You can, of course, move these test methods into the lower inner cases,
         * but you should understand that this will your tests harder to change
         * if you decide to change the behavior of the {@code TaskSearchController} class.
         */
        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            mockMvc.perform(get("/api/task/search")
                    .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
            )
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldReturnResultsAsJson() throws Exception {
            mockMvc.perform(get("/api/task/search")
                    .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
            )
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        }

        public class WhenNoTasksIsFound {

            @Before
            public void returnEmptyList()  {
                given(service.search(SEARCH_TERM)).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldReturnEmptyTaskList() throws Exception {
                mockMvc.perform(get("/api/task/search")
                        .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(jsonPath("$", hasSize(0)));
            }
        }

        public class WhenTwoTaskAreFound {

            private final Long FIRST_TASK_ID = 1L;
            private final String FIRST_TASK_TITLE = "firstTask";
            private final Long SECOND_TASK_ID = 33L;
            private final String SECOND_TASK_TITLE = "secondTask";

            TaskListDTO first;
            TaskListDTO second;

            @Before
            public void returnTwoTasks() {
                first = createTask(FIRST_TASK_ID, FIRST_TASK_TITLE, TaskStatus.OPEN);
                second = createTask(SECOND_TASK_ID, SECOND_TASK_TITLE, TaskStatus.OPEN);

                given(service.search(SEARCH_TERM)).willReturn(Arrays.asList(first, second));
            }

            /**
             * This factory method is found from the {@code TaskCrudControllerTest} class as well.
             * I used copy paste because I think that it makes these tests easier to read. However,
             * if there would be a third test class that requires this method, I would move it into
             * an object mother class.
             *
             * Also, I didn't use factory methods or test data builder because the {@code TaskListDTO}
             * objects are just dummy data containers.
             */
            private TaskListDTO createTask(Long id, String title, TaskStatus status) {
                TaskListDTO task = new TaskListDTO();

                task.setId(id);
                task.setTitle(title);
                task.setStatus(status);

                return task;
            }

            @Test
            public void shouldReturnListThatHasTwoTasks() throws Exception {
                mockMvc.perform(get("/api/task/search")
                        .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(jsonPath("$", hasSize(2)));
            }

            @Test
            public void shouldReturnCorrectTasks() throws Exception {
                mockMvc.perform(get("/api/task/search")
                        .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(jsonPath("$[0].id", is(FIRST_TASK_ID.intValue())))
                        .andExpect(jsonPath("$[0].title", is(FIRST_TASK_TITLE)))
                        .andExpect(jsonPath("$[0].status", is(TaskStatus.OPEN.toString())))
                        .andExpect(jsonPath("$[1].id", is(SECOND_TASK_ID.intValue())))
                        .andExpect(jsonPath("$[1].title", is(SECOND_TASK_TITLE)))
                        .andExpect(jsonPath("$[1].status", is(TaskStatus.OPEN.toString())));
            }
        }
    }
}
