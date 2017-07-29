package com.testwithspring.master.web

import com.testwithspring.master.UnitTest
import com.testwithspring.master.task.TaskCrudService
import com.testwithspring.master.task.TaskListDTO
import com.testwithspring.master.task.TaskStatus
import org.junit.experimental.categories.Category
import org.springframework.context.support.StaticMessageSource
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.testwithspring.master.web.WebTestConfig.exceptionResolver
import static com.testwithspring.master.web.WebTestConfig.fixedLocaleResolver
import static com.testwithspring.master.web.WebTestConfig.jspViewResolver
import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.contains
import static org.hamcrest.Matchers.hasProperty
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@Category(UnitTest.class)
class TaskCrudControllerSpec extends Specification {

    private static final TASK_ID = 1L;
    private static final TASK_TITLE = "title";

    def messageSource = new StaticMessageSource()
    def service = Mock(TaskCrudService)
    def mockMvc = MockMvcBuilders.standaloneSetup(new TaskCrudController(service, messageSource))
            .setHandlerExceptionResolvers(exceptionResolver())
            .setLocaleResolver(fixedLocaleResolver())
            .setViewResolvers(jspViewResolver())
            .build()

    def 'Show task list'() {

        def final FIRST_TASK_ID = 1L
        def final FIRST_TASK_TITLE = 'firstTask'
        def final SECOND_TASK_ID = 33L
        def final SECOND_TASK_TITLE = 'secondTask'

        def response

        when: 'The returned task list is irrelevant'
        1 * service.findAll() >> []

        and: 'A user opens the task list page'
        response = mockMvc.perform(get("/"))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the task list view'
        response.andExpect(view().name(WebTestConstants.View.TASK_LIST))

        when: 'No tasks is found'
        1 * service.findAll() >> []

        and: 'A user opens the task list page'
        response = mockMvc.perform(get("/"))

        then: 'Should show an empty task list'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)))

        when: 'Two tasks is found'
        1 * service.findAll() >> [
                new TaskListDTO(id: FIRST_TASK_ID, title: FIRST_TASK_TITLE, status: TaskStatus.OPEN),
                new TaskListDTO(id: SECOND_TASK_ID, title: SECOND_TASK_TITLE, status: TaskStatus.OPEN)
        ]

        and: 'A user opens the task list page'
        response = mockMvc.perform(get("/"))

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
