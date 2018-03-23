package com.testwithspring.master;

import com.testwithspring.master.config.Profiles;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@RestClientTest({MessageApiUrlBuilder.class, MessageService.class})
@ActiveProfiles(Profiles.UNIT_TEST)
@Tag(TestTags.UNIT_TEST)
class MessageServiceTest {

    private static final Long MESSAGE_ID = 99L;
    private static final String MESSAGE_TEXT = "Hello World!";

    @Autowired
    private MessageService service;

    @Autowired
    private MockRestServiceServer server;

    @BeforeEach
    void clearExpectations() {
        //We need to clear the state of the mock server
        //because we cannot add new expectations after
        //the actual request has been made.
        server.reset();
    }

    @Nested
    @DisplayName("Create a new message")
    class CreateMessage {

        private final String HEADER_NAME_CONTENT_TYPE = "Content-Type";
        private final String EXPECTED_URL = "http://localhost:9090/api/message";
        private final String RESPONSE_BODY = "{" +
                "\"id\": 99," +
                "\"text\": \"Hello World!\"" +
                "}";

        @BeforeEach
        void returnCreatedMessageAsJson() {
            //Unfortunately these assertions require that JUnit 4 jar is in the classpath.
            server.expect(times(1), method(HttpMethod.POST))
                    .andExpect(requestTo(EXPECTED_URL))
                    .andExpect(header(HEADER_NAME_CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.id", nullValue()))
                    .andExpect(jsonPath("$.text", is(MESSAGE_TEXT)))
                    .andRespond(withSuccess(RESPONSE_BODY, MediaType.APPLICATION_JSON_UTF8));
        }

        @Test
        @DisplayName("Should create a new message by invoking the remote API once")
        void shouldCreateNewMessageByInvokingRemoteApiOnce() {
            service.create(MESSAGE_TEXT);
            server.verify();
        }

        @Test
        @DisplayName("Should return a message that has the correct id")
        void shouldReturnMessageWithCorrectId() {
            Message created = service.create(MESSAGE_TEXT);
            assertThat(created.getId()).isEqualTo(MESSAGE_ID);
        }

        @Test
        @DisplayName("Should return a message that has the correct text")
        void shouldReturnMessageWithCorrectText() {
            Message created = service.create(MESSAGE_TEXT);
            assertThat(created.getText()).isEqualTo(MESSAGE_TEXT);
        }
    }

    @Nested
    @DisplayName("Find message")
    class FindMessage {

        private final String EXPECTED_URL = "http://localhost:9090/api/message/99";

        @Nested
        @DisplayName("When the message is not found")
        class WhenMessageIsNotFound {

            @BeforeEach
            void returnHttpStatusCodeNotFound() {
                server.expect(requestTo(EXPECTED_URL))
                        .andRespond(withStatus(HttpStatus.NOT_FOUND));
            }

            @Test
            @DisplayName("Should return an empty optional")
            void shouldReturnEmptyOptional() {
                Optional<Message> messageResult = service.findById(MESSAGE_ID);
                assertThat(messageResult).isEmpty();
            }
        }

        @Nested
        @DisplayName("When the message is found")
        class WhenMessageIsFound {

            private final String RESPONSE_BODY = "{" +
                    "\"id\": 99," +
                    "\"text\": \"Hello World!\"" +
                    "}";

            @BeforeEach
            void returnFoundMessage() {
                server.expect(requestTo(EXPECTED_URL))
                        .andRespond(withSuccess(RESPONSE_BODY, MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            @DisplayName("Should return a message that has the correct id")
            void shouldReturnMessageWithCorrectId() {
                Message found = service.findById(MESSAGE_ID).get();
                assertThat(found.getId()).isEqualTo(MESSAGE_ID);
            }

            @Test
            @DisplayName("Should return a message that has the correct text")
            void shouldReturnMessageWithCorrectText() {
                Message found = service.findById(MESSAGE_ID).get();
                assertThat(found.getText()).isEqualTo(MESSAGE_TEXT);
            }
        }
    }
}
