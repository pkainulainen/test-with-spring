package com.testwithspring.master.message

import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.config.ExampleApplicationContext
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@ContextConfiguration(classes = [ExampleApplicationContext.class])
@WebAppConfiguration
@Category(IntegrationTest.class)
class ShowMessageSpec extends Specification {

    @Autowired
    WebApplicationContext webAppContext

    def mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build()
    }

    def 'Show Message'() {

        def response

        when: 'A user open the show message page'
        response = mockMvc.perform(get("/"))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the show message view'
        response.andExpect(view().name("index"))

        and: 'Should forward the user to the show message page'
        println 'Integration Test: Should forward the user to the show message page'
        response.andExpect(forwardedUrl('/WEB-INF/jsp/index.jsp'))
    }
}
