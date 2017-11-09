package com.testwithspring.master.message

import com.testwithspring.master.IntegrationTest
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@SpringBootTest(classes = [MessageApplication.class])
@AutoConfigureMockMvc
@Category(IntegrationTest.class)
class ShowMessageSpec extends Specification {

    @Autowired
    MockMvc mockMvc

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
