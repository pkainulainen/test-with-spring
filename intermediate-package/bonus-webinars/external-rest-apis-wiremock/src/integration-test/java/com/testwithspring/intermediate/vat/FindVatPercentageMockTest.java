package com.testwithspring.intermediate.vat;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.testwithspring.intermediate.ExampleApplication;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.Profiles;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ExampleApplication.class})
@AutoConfigureMockMvc
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class FindVatPercentageMockTest {

    private static final String COUNTRY_CODE = "FI";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options()
            .bindAddress("localhost")
            .port(8080)
    );

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void configureDefaultResponse() {
        givenThat(any(anyUrl())
                .willReturn(ok())
        );
    }

    @Test
    public void shouldGetVatPercentageFromExternalApi() throws Exception {
        findByCountryCode(COUNTRY_CODE);

        verify(1, getRequestedFor(urlEqualTo("/api/external/vat-percentage?countryCode=FI")));
    }

    @Test
    public void shouldGetVatPercentageFromExternalApiTwo() throws Exception {
        findByCountryCode(COUNTRY_CODE);

        verify(1, getRequestedFor(urlMatching("/api/external/vat-percentage.+"))
                .withQueryParam("countryCode", equalTo("FI"))
        );
    }

    private ResultActions findByCountryCode(String countryCode) throws Exception {
        return mockMvc.perform(get("/api/vat-percentage")
                .param("countryCode", countryCode)
        );
    }
}
