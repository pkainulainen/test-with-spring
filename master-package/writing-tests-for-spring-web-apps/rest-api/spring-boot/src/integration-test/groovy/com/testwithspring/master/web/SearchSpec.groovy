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
class SearchSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Search tasks as an anonymous user'() {

        def response

        when: 'An anonymous user tries to search tasks'
        response = search(Tasks.SEARCH_TERM_ONE_MATCH)

        then: 'Should return the HTTP status code unauthorized'
        response.andExpect(MockMvcResultMatchers.status().isUnauthorized())

        and: 'Should return an empty response'
        response.andExpect(MockMvcResultMatchers.content().string(''))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Search tasks as a registered user'() {

        def response

        when: 'No tasks is found with the given search term'
        response = search(Tasks.SEARCH_TERM_NOT_FOUND)

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return search results as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return zero tasks'
        response.andExpect(MockMvcResultMatchers.jsonPath('$', hasSize(0)))

        when: 'One task is found with the given search term'
        response = search(Tasks.SEARCH_TERM_ONE_MATCH)

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return search results as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one task'
        response.andExpect(MockMvcResultMatchers.jsonPath('$', hasSize(1)))
        
        and: 'Should return the information of the found task'
        response .andExpect(MockMvcResultMatchers.jsonPath('$[0].id', is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].status', is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].title', is(Tasks.WriteLesson.TITLE)))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Search tasks as an admin'() {

        def response

        when: 'No tasks is found with the given search term'
        response = search(Tasks.SEARCH_TERM_NOT_FOUND)

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return search results as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return zero tasks'
        response.andExpect(MockMvcResultMatchers.jsonPath('$', hasSize(0)))

        when: 'One task is found with the given search term'
        response = search(Tasks.SEARCH_TERM_ONE_MATCH)

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should return search results as JSON'
        response.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

        and: 'Should return one task'
        response.andExpect(MockMvcResultMatchers.jsonPath('$', hasSize(1)))

        and: 'Should return the information of the found task'
        response .andExpect(MockMvcResultMatchers.jsonPath('$[0].id', is(Tasks.WriteLesson.ID.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].status', is(Tasks.WriteLesson.STATUS.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath('$[0].title', is(Tasks.WriteLesson.TITLE)))
    }

    private ResultActions search(searchTerm) {
        return mockMvc.perform(MockMvcRequestBuilders.get('/api/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, searchTerm)
        )
    }
}
