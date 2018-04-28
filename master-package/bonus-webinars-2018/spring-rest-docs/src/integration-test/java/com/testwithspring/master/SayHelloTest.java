package com.testwithspring.master;

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

import java.nio.charset.Charset;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExampleApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Tag(TestTags.INTEGRATION_TEST)
class SayHelloTest {

    private static final MediaType TEXT_PLAIN = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return the HTTP status code OK")
    void shouldReturnHttpStatusCodeOk() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return the message as plain text")
    void shouldReturnMessageAsPlainText() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(content().contentType(TEXT_PLAIN));
    }

    @Test
    @DisplayName("Should return the Hello World! message")
    void shouldReturnHelloWorldMessage() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(content().string("Hello World!"));
    }

    @Test
    @DisplayName("Create the Spring REST Docs snippets")
    void createSpringRESTDocsSnippets() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andDo(document("say-hello"));
    }
}
