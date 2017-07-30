package com.testwithspring.master.web

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@Category(UnitTest.class)
class LoginControllerSpec extends Specification {

    def mockMvc = MockMvcBuilders.standaloneSetup(new LoginController())
            .build()

    def 'Render login page'() {

        when: 'A user opens the login page'
        def response = mockMvc.perform(get('/user/login'))

        then: 'Should return the HTTP status code OK'
        response.andExpect(status().isOk())

        and: 'Should render the login view'
        response.andExpect(view().name(WebTestConstants.View.LOGIN))
    }
}
