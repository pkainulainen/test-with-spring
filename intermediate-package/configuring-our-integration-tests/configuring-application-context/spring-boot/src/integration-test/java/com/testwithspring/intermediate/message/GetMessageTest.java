package com.testwithspring.intermediate.message;

import com.testwithspring.intermediate.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MessageApplication.class})
@Category(IntegrationTest.class)
public class GetMessageTest {

    @Autowired
    private MessageService service;

    @Test
    public void shouldReturnHelloWorldMessage() {
        String message = service.getMessage();
        assertThat(message).isEqualTo("Hello World!");
    }
}
