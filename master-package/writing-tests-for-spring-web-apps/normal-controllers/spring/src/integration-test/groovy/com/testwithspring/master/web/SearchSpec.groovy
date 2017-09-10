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
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.Matchers.allOf
import static org.hamcrest.Matchers.hasItem
import static org.hamcrest.Matchers.hasProperty
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.Matchers.is
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@ContextConfiguration(classes = [IntegrationTestContext.class])
@WebAppConfiguration
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
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
                .apply(springSecurity())
                .build()
    }

    def 'Search tasks as an anonymous user'() {

        def response

        when: 'An anonymous user submits the search form'
        response = submitSearchForm(Tasks.SEARCH_TERM_ONE_MATCH)

        then: 'Should return the HTTP status code found'
        response.andExpect(status().isFound())

        and: 'Should redirect the user to the login page'
        response.andExpect(redirectedUrl(WebTestConstants.LOGIN_PAGE_URL))
    }

    @WithUserDetails(Users.JohnDoe.EMAIL_ADDRESS)
    def 'Search tasks as a registered user'() {

        def response

        when: 'No tasks is found with the used search term'
        response = submitSearchForm(Tasks.SEARCH_TERM_NOT_FOUND)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the search result view'
        response.andExpect(view().name(WebTestConstants.View.SEARCH_RESULTS))

        and: 'Should forward the user to the search result page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/search-results.jsp'))

        and: 'Should not display any tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)))

        and: 'Should display the used search term'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.SEARCH_TERM,
                is(Tasks.SEARCH_TERM_NOT_FOUND)
        ))

        when: 'One task is found with the given search term'
        response = submitSearchForm(Tasks.SEARCH_TERM_ONE_MATCH)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the search result view'
        response.andExpect(view().name(WebTestConstants.View.SEARCH_RESULTS))

        and: 'Should forward the user to the search result page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/search-results.jsp'))

        and: 'Should display one task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(1)))

        and: 'Should display the information of the found task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasItem(allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS))
        ))))

        and: 'Should display the used search term'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.SEARCH_TERM,
                is(Tasks.SEARCH_TERM_ONE_MATCH)
        ))
    }

    @WithUserDetails(Users.AnneAdmin.EMAIL_ADDRESS)
    def 'Search tasks as an administrator'() {

        def response

        when: 'No tasks is found with the used search term'
        response = submitSearchForm(Tasks.SEARCH_TERM_NOT_FOUND)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the search result view'
        response.andExpect(view().name(WebTestConstants.View.SEARCH_RESULTS))

        and: 'Should forward the user to the search result page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/search-results.jsp'))

        and: 'Should not display any tasks'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(0)))

        and: 'Should display the used search term'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.SEARCH_TERM,
                is(Tasks.SEARCH_TERM_NOT_FOUND)
        ))

        when: 'One task is found with the given search term'
        response = submitSearchForm(Tasks.SEARCH_TERM_ONE_MATCH)

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the search result view'
        response.andExpect(view().name(WebTestConstants.View.SEARCH_RESULTS))

        and: 'Should forward the user to the search result page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/task/search-results.jsp'))

        and: 'Should display one task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasSize(1)))

        and: 'Should display the information of the found task'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.TASK_LIST, hasItem(allOf(
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.ID, is(Tasks.WriteLesson.ID)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.TITLE, is(Tasks.WriteLesson.TITLE)),
                hasProperty(WebTestConstants.ModelAttributeProperty.Task.STATUS, is(Tasks.WriteLesson.STATUS))
        ))))

        and: 'Should display the used search term'
        response.andExpect(model().attribute(WebTestConstants.ModelAttributeName.SEARCH_TERM,
                is(Tasks.SEARCH_TERM_ONE_MATCH)
        ))
    }

    private ResultActions submitSearchForm(searchTerm) throws Exception {
        return mockMvc.perform(post('/task/search')
                .param(WebTestConstants.RequestParameter.SEARCH_TERM, searchTerm)
                .with(csrf())
        )
    }
}
