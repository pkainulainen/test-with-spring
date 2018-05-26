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

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExampleApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Tag(TestTags.INTEGRATION_TEST)
class CreateMessageWithValidMessageTextTest {

    private final String MESSAGE_TEXT = "Hello World!";

    @Autowired
    private MockMvc mockMvc;

    private MessageDTO input;

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
