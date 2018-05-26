package com.testwithspring.master;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExampleApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Tag(TestTags.INTEGRATION_TEST)
class CreateMessageWithEmptyMessageTextTest {

    @Autowired
    private MockMvc mockMvc;

    private MessageDTO input;

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
