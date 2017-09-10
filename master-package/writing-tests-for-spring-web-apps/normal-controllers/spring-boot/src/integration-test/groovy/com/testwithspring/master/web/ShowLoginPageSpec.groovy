package com.testwithspring.master.web

import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationTestContext.class])
@AutoConfigureMockMvc
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class ShowLoginPageSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def 'Show login page'() {

        def response

        when: 'A user opens the login page'
        response = mockMvc.perform(MockMvcRequestBuilders.get('/user/login'))

        then: 'Should return the HTTP status code OK'
        response.andExpect(MockMvcResultMatchers.status().isOk())

        and: 'Should render the login view'
        response.andExpect(MockMvcResultMatchers.view().name(WebTestConstants.View.LOGIN))
     }
}
