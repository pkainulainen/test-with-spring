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

import static com.testwithspring.intermediate.TestDoubles.stub;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class VatPercentageControllerTest {

    private MockMvc mockMvc;
    private VatPercentageService service;

    @Before
    public void configureSystemUnderTest() {
        service = stub(VatPercentageService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new VatPercentageController(service))
                .build();
    }

    public class FindByCountryCode {

        private final String COUNTRY_CODE = "FI";
        private final Integer VAT_PERCENTAGE = 24;

        @Before
        public void returnVatPercentage() {
            VatPercentage found = new VatPercentage();
            found.setCountryCode(COUNTRY_CODE);
            found.setVatPercentage(VAT_PERCENTAGE);

            given(service.findByCountryCode(COUNTRY_CODE)).willReturn(found);
        }

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
                    .andExpect(jsonPath("$.vatPercentage", is(VAT_PERCENTAGE)));
        }

        private ResultActions findVatPercentage(String countryCode) throws Exception {
            return mockMvc.perform(get("/api/vat-percentage")
                    .param("countryCode", countryCode)
            );
        }
    }
}
