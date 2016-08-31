package com.testwithspring.starter.springboot.web;

import com.testwithspring.starter.springboot.UnitTest;
import com.testwithspring.starter.springboot.task.TaskListDTO;
import com.testwithspring.starter.springboot.task.TaskSearchService;
import com.testwithspring.starter.springboot.task.TaskStatus;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static com.testwithspring.starter.springboot.TestDoubles.stub;
import static com.testwithspring.starter.springboot.web.WebTestConfig.viewResolver;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
                .setViewResolvers(viewResolver())
                .build();
    }

    public class ShowSearchResults {

        //Model attributes
        private final String MODEL_ATTRIBUTE_NAME_TASKS = "tasks";

        //Request parameters
        private final String REQUEST_PARAMETER_SEARCH_TERM = "searchTerm";

        //Views
        private final String VIEW_NAME_SEARCH_RESULT_VIEW = "task/search-results";

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
            mockMvc.perform(post("/task/search")
                    .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
            )
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderSearchResultView() throws Exception {
            mockMvc.perform(post("/task/search")
                    .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
            )
                    .andExpect(view().name(VIEW_NAME_SEARCH_RESULT_VIEW));
        }

        public class WhenNoTasksIsFound {

            @Before
            public void returnEmptyList()  {
                given(service.search(SEARCH_TERM)).willReturn(new ArrayList<>());
            }

            @Test
            public void shouldShowEmptyTaskList() throws Exception {
                mockMvc.perform(post("/task/search")
                        .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, hasSize(0)));
            }
        }

        public class WhenTwoTasksAreFound {

            //Model attribute fields
            private static final String TASK_PROPERTY_NAME_ID = "id";
            private static final String TASK_PROPERTY_NAME_STATUS = "status";
            private static final String TASK_PROPERTY_NAME_TITLE = "title";

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
            public void shouldShowTaskListThatHasTwoTasks() throws Exception {
                mockMvc.perform(post("/task/search")
                        .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, hasSize(2)));
            }

            @Test
            public void shouldShowTwoTasksInCorrectOrder() throws Exception {
                mockMvc.perform(post("/task/search")
                        .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, contains(first, second)));
            }

            @Test
            public void shouldShowCorrectInformation() throws Exception {
                mockMvc.perform(post("/task/search")
                        .param(REQUEST_PARAMETER_SEARCH_TERM, SEARCH_TERM)
                )
                        .andExpect(model().attribute(MODEL_ATTRIBUTE_NAME_TASKS, allOf(
                                hasItem(allOf(
                                        hasProperty(TASK_PROPERTY_NAME_ID, is(FIRST_TASK_ID)),
                                        hasProperty(TASK_PROPERTY_NAME_TITLE, is(FIRST_TASK_TITLE)),
                                        hasProperty(TASK_PROPERTY_NAME_STATUS, is(TaskStatus.OPEN))
                                )),
                                hasItem(allOf(
                                        hasProperty(TASK_PROPERTY_NAME_ID, is(SECOND_TASK_ID)),
                                        hasProperty(TASK_PROPERTY_NAME_TITLE, is(SECOND_TASK_TITLE)),
                                        hasProperty(TASK_PROPERTY_NAME_STATUS, is(TaskStatus.OPEN))
                                ))
                        )));
            }
        }
    }
}
