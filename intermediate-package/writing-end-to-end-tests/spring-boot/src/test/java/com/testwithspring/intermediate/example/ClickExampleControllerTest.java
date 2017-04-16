package com.testwithspring.intermediate.example;

import com.testwithspring.intermediate.UnitTest;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class ClickExampleControllerTest {

    private MockMvc mockMvc;

    @Before
    public void configureSystemUnderTest() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ClickExampleController())
                .setViewResolvers(WebTestConfig.viewResolver())
                .build();
    }

    public class RenderClickSourceView {

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            renderClickSourceView()
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderClickSourceView() throws Exception {
            renderClickSourceView()
                    .andExpect(view().name(WebTestConstants.Views.CLICK_SOURCE_VIEW));
        }

        private ResultActions renderClickSourceView() throws Exception {
            return mockMvc.perform(get("/click-source"));
        }
    }

    public class RenderClickTargetView {

        @Test
        public void shouldReturnHttpStatusCodeOk() throws Exception {
            renderClickTargetView()
                    .andExpect(status().isOk());
        }

        @Test
        public void shouldRenderClickTargetView() throws Exception {
            renderClickTargetView()
                    .andExpect(view().name(WebTestConstants.Views.CLICK_TARGET_VIEW));
        }

        private ResultActions renderClickTargetView() throws Exception {
            return mockMvc.perform(get("/click-target"));
        }
    }
}
