package com.testwithspring.master;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@Tag(TestTags.UNIT_TEST)
class MessageControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest(RestDocumentationExtension restDocumentation) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MessageController())
                .setControllerAdvice(new ValidationErrorHandler())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Nested
    @DisplayName("Create a new message")
    class CreateNewMessage {

        private MessageDTO input;

        @Nested
        @DisplayName("When empty message text is given")
        class WhenEmptyMessageTextIsGiven {

            @BeforeEach
            void createInput() {
                input = new MessageDTO();
                input.setText("");
            }

            @Test
            @DisplayName("should return the HTTP status code bad request")
            void shouldReturnHttpStatusCodeBadRequest() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("should return validation errors as JSON")
            void shouldReturnValidationErrorsAsJson() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            @DisplayName("should return one validation error")
            void shouldReturnOneValidationError() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors", hasSize(1)));
            }

            @Test
            @DisplayName("Should return a validation error about an empty message text")
            void shouldReturnValidationErrorAboutEmptyTitle() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.fieldErrors[0].field", is("text")))
                        .andExpect(jsonPath("$.fieldErrors[0].errorCode", is("NotBlank")));
            }

            @Test
            @DisplayName("Create the Spring REST Docs snippets")
            void createSpringRESTDocsSnippets() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andDo(document("create-message-empty-text"));
            }
        }

        @Nested
        @DisplayName("When valid message text is given")
        class WhenValidMessageTextIsGiven {

            private final String MESSAGE_TEXT = "Hello World!";

            @BeforeEach
            void createInput() {
                input = new MessageDTO();
                input.setText(MESSAGE_TEXT);
            }

            @Test
            @DisplayName("Should return the HTTP status code created")
            void shouldReturnHttpStatusCodeCreated() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(status().isCreated());
            }

            @Test
            @DisplayName("Should return the created message as JSON")
            void shouldReturnCreatedMessageAsJson() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
            }

            @Test
            @DisplayName("Should return created message with the correct message text")
            void shouldReturnCreatedMessageWithCorrectMessageText() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andExpect(jsonPath("$.text", is(MESSAGE_TEXT)));
            }

            @Test
            @DisplayName("Create the Spring REST Docs snippets")
            void createSpringRESTDocsSnippets() throws Exception {
                mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(WebTestUtil.convertObjectToJsonBytes(input))
                )
                        .andDo(document("create-message-success"));
            }
        }
    }
}
