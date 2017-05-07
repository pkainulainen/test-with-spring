package com.testwithspring.intermediate.vat;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;

import static com.testwithspring.intermediate.TestDoubles.stub;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class VatPercentageServiceTest {

    private RestTemplate restTemplate;
    private VatPercentageService service;

    @Before
    public void configureSystemUnderTest() {
        restTemplate = stub(RestTemplate.class);
        service = new VatPercentageService(restTemplate);
    }

    public class FindByCountryCode {

        private final String COUNTRY_CODE = "FI";
        private final Integer VAT_PERCENTAGE = 24;

        @Before
        public void returnVatPercentage() {
            VatPercentage found = new VatPercentage();
            found.setCountryCode(COUNTRY_CODE);
            found.setVatPercentage(VAT_PERCENTAGE);

            given(restTemplate.getForObject(
                    "http://localhost:8080/api/external/vat-percentage?countryCode=FI",
                    VatPercentage.class
            )).willReturn(found);
        }

        @Test
        public void shouldReturnCorrectVatPercentage() {
            VatPercentage vatPercentage = service.findByCountryCode(COUNTRY_CODE);

            assertThat(vatPercentage.getCountryCode()).isEqualTo(COUNTRY_CODE);
            assertThat(vatPercentage.getVatPercentage()).isEqualTo(VAT_PERCENTAGE);
        }
    }
}
