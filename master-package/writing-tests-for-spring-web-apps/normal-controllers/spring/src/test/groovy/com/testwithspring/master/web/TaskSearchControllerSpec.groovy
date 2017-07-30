package com.testwithspring.master.web

import com.testwithspring.master.UnitTest
import com.testwithspring.master.task.TaskListDTO
import com.testwithspring.master.task.TaskSearchService
import com.testwithspring.master.task.TaskStatus
import org.junit.experimental.categories.Category
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.testwithspring.master.web.WebTestConfig.jspViewResolver
import static org.hamcrest.Matchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@Category(UnitTest.class)
class TaskSearchControllerSpec extends Specification {

    private static final FIRST_TASK_ID = 1L
    private static final FIRST_TASK_TITLE = 'firstTask'
    private static final SECOND_TASK_ID = 33L
    private static final SECOND_TASK_TITLE = 'secondTask'
    private static final SEARCH_TERM = 'tas'
    private static final SEARCH_TERM_NO_RESULTS = 'noResults'

    def service = Stub(TaskSearchService)
    def mockMvc = MockMvcBuilders.standaloneSetup(new TaskSearchController(service))
            .setViewResolvers(jspViewResolver())
            .build()

    def 'Show search results'() {

        def response

        when: 'A user submits the search form'
        response = mockMvc.perform(post('/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, SEARCH_TERM)
        )

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the search result view'
        response.andExpect(view().name(WebTestConstants.View.SEARCH_RESULTS))

        and: 'Should show the used search term'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.SEARCH_TERM, is(SEARCH_TERM)))

        when: 'No tasks is found'
        service.search(SEARCH_TERM_NO_RESULTS) >> []

        and: 'A user submits the search form'
        response = mockMvc.perform(post('/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, SEARCH_TERM_NO_RESULTS)
        )

        then: 'Should show an empty task list'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)))

        when: 'Two tasks is found'
        service.search(SEARCH_TERM) >> [
                new TaskListDTO(id: FIRST_TASK_ID, title: FIRST_TASK_TITLE, status: TaskStatus.OPEN),
                new TaskListDTO(id: SECOND_TASK_ID, title: SECOND_TASK_TITLE, status: TaskStatus.OPEN)
        ]

        and: 'A user submits the search form'
        response = mockMvc.perform(post('/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, SEARCH_TERM)
        )

        then: 'Should show task list that has two tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(2)))

        and: 'Should show task list that contains the correct information'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, contains(
                allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(FIRST_TASK_ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(FIRST_TASK_TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.OPEN))
                ),
                allOf(
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(SECOND_TASK_ID)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(SECOND_TASK_TITLE)),
                        hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(TaskStatus.OPEN))
                )
        )))
    }
}
