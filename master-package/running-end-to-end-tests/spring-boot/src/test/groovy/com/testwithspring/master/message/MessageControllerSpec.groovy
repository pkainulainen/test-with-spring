package com.testwithspring.master.message

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@Category(UnitTest.class)
class MessageControllerSpec extends Specification {

    def mockMvc = MockMvcBuilders.standaloneSetup(new MessageController())
            .setViewResolvers(WebTestConfig.viewResolver())
            .build()

    def 'Show Message'() {

        def response

        when: 'A user open the show message page'
        response = mockMvc.perform(get("/"))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the show message view'
        response.andExpect(view().name("index"))
    }
}
