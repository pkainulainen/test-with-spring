package com.testwithspring.intermediate.vat;

import com.github.tomakehurst.wiremock.client.WireMock;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ExampleApplication.class})
@AutoConfigureMockMvc
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class FindVatPercentageStubTest {

    private static final String COUNTRY_CODE = "FI";
    private static final Integer VAT_PERCENTAGE = 24;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options()
            .bindAddress("localhost")
            .port(8080)
    );

    @Autowired
    private MockMvc mockMvc;

    //This is the most verbose stubbing syntax but I think that it is useful to know
    //it even though I don't recommend that we should use it.
    /*@Before
    public void returnVatPercentage() {
        givenThat(WireMock.get(
                urlEqualTo("/api/external/vat-percentage?countryCode=FI")
        )
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody("{\"countryCode\": \"FI\",\"vatPercentage\": 24}")
                )
        );
    }*/

    //This is an improved version of the stubbing syntax. Here we use the combination of
    //URL regexp and parameter assertion when we configure the returned JSON.
    /*@Before
    public void returnVatPercentage() {
        givenThat(WireMock.get(
                urlMatching("/api/external/vat-percentage.+")
        )
                        .withQueryParam("countryCode", equalTo("FI"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type","application/json;charset=UTF-8")
                                .withBody("{\"countryCode\": \"FI\",\"vatPercentage\": 24"}")
                        )
        );
    }*/

    //This is the least verbose stubbing syntax and I recommend that we should use this
    //syntax.
    @Before
    public void returnVatPercentage() {
        givenThat(WireMock.get(
                urlMatching("/api/external/vat-percentage.+")
                )
                        .withQueryParam("countryCode", equalTo("FI"))
                        .willReturn(okJson("{\"countryCode\": \"FI\",\"vatPercentage\": 24}"))
        );
    }

    @Test
    public void shouldReturnHttpStatusCodeOk() throws Exception {
        findByCountryCode(COUNTRY_CODE)
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnVatPercentageAsJson() throws Exception {
        findByCountryCode(COUNTRY_CODE)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldReturnCorrectVatPercentage() throws Exception {
        findByCountryCode(COUNTRY_CODE)
                .andExpect(jsonPath("$.countryCode", is(COUNTRY_CODE)))
                .andExpect(jsonPath("$.vatPercentage", is(VAT_PERCENTAGE)));
    }

    private ResultActions findByCountryCode(String countryCode) throws Exception {
        return mockMvc.perform(get("/api/vat-percentage")
                .param("countryCode", countryCode)
        );
    }
}
