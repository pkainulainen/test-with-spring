package com.testwithspring.master.web

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
import com.testwithspring.master.Tasks
import com.testwithspring.master.Users
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is

@SpringBootTest(classes = [IntegrationTestContext.class])
@AutoConfigureMockMvc
@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DatabaseSetup([
        '/com/testwithspring/master/users.xml',
        '/com/testwithspring/master/tasks.xml'
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class FindTaskListSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Find task list as an anonymous user'() {

        def response

        when: 'An anonymous user tries to find the task list'
        response = findAllTasks()

        then: 'Should return the HTTP status unauthorized'
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(MockMvcResultMatchers.content().string(''))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Find task list as a registered user'() {

        def response

        when: 'A registered user finds the task list'
        response = findAllTasks()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return search results as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return two tasks'
        response.andExpect(MockMvcResultMatchers.jsonPath('$', hasSize(2)))

        and: 'Should return the found tasks'
        response.andExpect(MockMvcResultMatchers.jsonPath('$[0].id', is(Tasks.WriteExampleApp.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].status', is(Tasks.WriteExampleApp.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].title', is(Tasks.WriteExampleApp.TITLE)))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].id', is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].status', is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].title', is(Tasks.WriteLesson.TITLE)))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Find task list as an administrator'() {

        def response

        when: 'An administrator finds the task list'
        response = findAllTasks()

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return search results as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return two tasks'
        response.andExpect(MockMvcResultMatchers.jsonPath('$', hasSize(2)))

        and: 'Should return the found tasks'
        response.andExpect(MockMvcResultMatchers.jsonPath('$[0].id', is(Tasks.WriteExampleApp.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].status', is(Tasks.WriteExampleApp.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].title', is(Tasks.WriteExampleApp.TITLE)))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].id', is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].status', is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[1].title', is(Tasks.WriteLesson.TITLE)))
    }

    private ResultActions findAllTasks() {
        return mockMvc.perform(MockMvcRequestBuilders.get('/api/task'))
    }
}
