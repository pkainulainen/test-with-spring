package com.testwithspring.intermediate.message;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestContext;
import com.testwithspring.intermediate.ReplacementDataSetLoader;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IntegrationTestContext.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DatabaseSetup("messages.xml")
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
public class FindMessageByIdTest {

    @Autowired
    private MessageRepository repository;

    @Test
    public void shouldReturnOptionalThatContainsFoundMessageWhenMessageIsFound() {
        Optional<Message> found = repository.findById(Messages.HelloWorld.ID);
        assertThat(found.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnCorrectInformationWhenMessageIsFound() {
        Message found = repository.findById(Messages.HelloWorld.ID).get();

        assertThat(found.getId()).isEqualTo(Messages.HelloWorld.ID);
        assertThat(found.getMessageText()).isEqualTo(Messages.HelloWorld.MESSAGE_TEXT);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenMessageIsNotFound() {
        Optional<Message> found = repository.findById(Messages.UNKNOWN_MESSAGE_ID);
        assertThat(found.isPresent()).isFalse();
    }
}