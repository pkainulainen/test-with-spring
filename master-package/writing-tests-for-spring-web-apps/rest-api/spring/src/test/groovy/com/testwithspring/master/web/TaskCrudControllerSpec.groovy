package com.testwithspring.master.web

import com.testwithspring.master.UnitTest
import com.testwithspring.master.task.TaskCrudService
import com.testwithspring.master.task.TaskListDTO
import com.testwithspring.master.task.TaskStatus
import org.junit.experimental.categories.Category
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.ZonedDateTime

import static com.testwithspring.master.web.WebTestConfig.fixedLocaleResolver
import static com.testwithspring.master.web.WebTestConfig.objectMapperHttpMessageConverter
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Category(UnitTest.class)
class TaskCrudControllerSpec extends Specification {
    
    def service = Mock(TaskCrudService)
    def mockMvc = MockMvcBuilders.standaloneSetup(new TaskCrudController(service))
            .setControllerAdvice(new TaskErrorHandler())
            .setLocaleResolver(fixedLocaleResolver())
            .setMessageConverters(objectMapperHttpMessageConverter())
            .build();

    def 'Find all tasks'() {

        def final FIRST_TASK_ID = 1L
        def final FIRST_TASK_TITLE = 'firstTask'
        def final SECOND_TASK_ID = 33L
        def final SECOND_TASK_TITLE = 'secondTask'

        def response

        when: 'The returned task list is irrelevant'
        1 * service.findAll() >> []

        and: 'A user gets the task list'
        response = mockMvc.perform(get('/api/task'))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should return found tasks as JSON'
        response.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))

        when: 'No tasks is found'
        1 * service.findAll() >> []

        and: 'A user gets the task list'
        response = mockMvc.perform(get('/api/task'))

        then: 'Should return empty list'
        response.andExpect(jsonPath('$', hasSize(0)))

        when: 'Two tasks are found'
        1 * service.findAll() >> [
                new TaskListDTO(id: FIRST_TASK_ID, title: FIRST_TASK_TITLE, status: TaskStatus.OPEN),
                new TaskListDTO(id: SECOND_TASK_ID, title: SECOND_TASK_TITLE, status: TaskStatus.OPEN)
        ]

        and: 'A user gets the task list'
        response = mockMvc.perform(get('/api/task'))

        then: 'Should return two tasks'
        response.andExpect(jsonPath('$', hasSize(2)))

        and: 'Should return the correct tasks'
        response.andExpect(jsonPath('$[0].id', is(FIRST_TASK_ID.intValue())))
                .andExpect(jsonPath('$[0].title', is(FIRST_TASK_TITLE)))
                .andExpect(jsonPath('$[0].status', is(TaskStatus.OPEN.toString())))
                .andExpect(jsonPath('$[1].id', is(SECOND_TASK_ID.intValue())))
                .andExpect(jsonPath('$[1].title', is(SECOND_TASK_TITLE)))
                .andExpect(jsonPath('$[1].status', is(TaskStatus.OPEN.toString())));
    }
}
