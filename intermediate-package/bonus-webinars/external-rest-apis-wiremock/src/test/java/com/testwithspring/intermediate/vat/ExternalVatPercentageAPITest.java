package com.testwithspring.intermediate.vat;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class ExternalVatPercentageAPITest {

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ExternalVatPercentageAPI())
                .build();
    }

    public class FindByCountryCode {

        private final String COUNTRY_CODE = "FI";
        private final Integer EXPECTED_VAT_PERCENTAGE = 23;

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            findVatPercentage(COUNTRY_CODE)
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldReturnVatPercentageAsJson() throws Exception {
            findVatPercentage(COUNTRY_CODE)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        }

        @Test
        public void shouldReturnCorrectVatPercentage() throws Exception {
            findVatPercentage(COUNTRY_CODE)
                    .andExpect(jsonPath("$.countryCode", is(COUNTRY_CODE)))
                    .andExpect(jsonPath("$.vatPercentage", is(EXPECTED_VAT_PERCENTAGE)));
        }

        private ResultActions findVatPercentage(String countryCode) throws Exception {
            return mockMvc.perform(get("/api/external/vat-percentage")
                    .param("countryCode", countryCode)
            );
        }
    }
}
