package com.testwithspring.master;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@Tag(TestTags.UNIT_TEST)
class HelloWorldControllerTest {

    private static final MediaType TEXT_PLAIN = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("ISO-8859-1"));

    private MockMvc mockMvc;

    @BeforeEach
    void configureSystemUnderTest(RestDocumentationExtension restDocumentation) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new HelloWorldController())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Nested
    @DisplayName("Say Hello")
    class SayHello {

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
}
