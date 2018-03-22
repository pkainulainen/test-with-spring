package com.testwithspring.master;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@Tag(TestTags.UNIT_TEST)
@DisplayName("Build message API urls")
class MessageApiUrlBuilderTest {

    private static final String API_BASE_URL = "http://localhost:8080";

    private MessageApiUrlBuilder urlBuilder;

    @BeforeEach
    void createUrlBuilder() {
        urlBuilder = new MessageApiUrlBuilder(API_BASE_URL);
    }

    @Nested
    @DisplayName("Build find message url")
    class BuildFindMessageUrl {

        private final String EXPECTED_URL = "http://localhost:8080/api/message/99";
        private final Long MESSAGE_ID = 99L;

        @Test
        @DisplayName("Should return the correct url")
        void shouldReturnCorrectUrl() {
            String url = urlBuilder.buildFindMessageUrl(MESSAGE_ID);
            assertThat(url).isEqualTo(EXPECTED_URL);
        }
    }
}
