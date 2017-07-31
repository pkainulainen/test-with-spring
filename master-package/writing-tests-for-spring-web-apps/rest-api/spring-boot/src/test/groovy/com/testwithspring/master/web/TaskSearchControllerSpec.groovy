package com.testwithspring.master.web

import com.testwithspring.master.UnitTest
import com.testwithspring.master.task.TaskListDTO
import com.testwithspring.master.task.TaskSearchService
import com.testwithspring.master.task.TaskStatus
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.testwithspring.master.web.WebTestConfig.objectMapperHttpMessageConverter
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(UnitTest.class)
class TaskSearchControllerSpec extends Specification {

    private static final FIRST_TASK_ID = 1L
    private static final FIRST_TASK_TITLE = 'firstTask'
    private static final SECOND_TASK_ID = 33L
    private static final SECOND_TASK_TITLE = 'secondTask'
    private static final SEARCH_TERM = 'tas'
    private static final SEARCH_TERM_NO_RESULTS = 'noResults'

    def service = Stub(TaskSearchService)
    def mockMvc =  MockMvcBuilders.standaloneSetup(new TaskSearchController(service))
            .setMessageConverters(objectMapperHttpMessageConverter())
            .build()

    def 'Search'() {

        def response

        when: 'A user performs the search'
        response = mockMvc.perform(get('/api/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, SEARCH_TERM)
        )

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return search results as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        when: 'No tasks is found'
        service.search(SEARCH_TERM_NO_RESULTS) >> []

        and: 'A user performs the search'
        response = mockMvc.perform(get('/api/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, SEARCH_TERM_NO_RESULTS)
        )

        then: 'Should return an empty list'
        response.andExpect(jsonPath('$', hasSize(0)))

        when: 'Two tasks are found'
        service.search(SEARCH_TERM) >> [
                new TaskListDTO(id: FIRST_TASK_ID, title: FIRST_TASK_TITLE, status: TaskStatus.OPEN),
                new TaskListDTO(id: SECOND_TASK_ID, title: SECOND_TASK_TITLE, status: TaskStatus.OPEN)
        ]

        and: 'A user performs the search'
        response = mockMvc.perform(get('/api/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, SEARCH_TERM)
        )

        then: 'Should return two tasks'
        response.andExpect(jsonPath('$', hasSize(2)))

        and: 'Should return the correct tasks'
        response.andExpect(jsonPath('$[0].id', is(FIRST_TASK_ID.intValue())))
                .andExpect(jsonPath('$[0].title', is(FIRST_TASK_TITLE)))
                .andExpect(jsonPath('$[0].status', is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath('$[1].id', is(SECOND_TASK_ID.intValue())))
                .andExpect(jsonPath('$[1].title', is(SECOND_TASK_TITLE)))
                .andExpect(jsonPath('$[1].status', is(TaskStatus.OPEN.toString())))
    }
}
