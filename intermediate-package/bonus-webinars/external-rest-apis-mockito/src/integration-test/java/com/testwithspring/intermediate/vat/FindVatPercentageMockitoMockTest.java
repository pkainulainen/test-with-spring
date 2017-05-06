package com.testwithspring.intermediate.vat;

import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestMockitoMockContext;
import com.testwithspring.intermediate.Profiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IntegrationTestMockitoMockContext.class})
@AutoConfigureMockMvc
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class FindVatPercentageMockitoMockTest {

    private static final String COUNTRY_CODE = "FI";
    private static final Integer VAT_PERCENTAGE = 24;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VatPercentageService service;

    @Before
    public void configureTestCases() {
        resetMock();
        returnVatPercentage();
    }

    private void resetMock() {
        Mockito.reset(service);
    }

    private void returnVatPercentage() {
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

    @Test
    public void shouldFindVatPercentageWithCorrectCountryCode() throws Exception {
        findVatPercentage(COUNTRY_CODE);

        verify(service, times(1)).findByCountryCode(COUNTRY_CODE);
    }

    private ResultActions findVatPercentage(String countryCode) throws Exception {
        return mockMvc.perform(get("/api/vat-percentage")
                .param("countryCode", countryCode)
        );
    }
}
