package com.testwithspring.intermediate.task;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.IntegrationTestContext;
import com.testwithspring.intermediate.ReplacementDataSetLoader;
import com.testwithspring.intermediate.Tasks;
import com.testwithspring.intermediate.config.Profiles;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {IntegrationTestContext.class})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        ServletTestExecutionListener.class
})
@DatabaseSetup({
        "/com/testwithspring/intermediate/users.xml",
        "/com/testwithspring/intermediate/tasks.xml"
})
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class FindAllTasksWhenTwoTasksAreFoundTest {

    @Autowired
    private TaskRepository repository;

    @Test
    public void shouldReturnTwoTasks() {
        List<TaskListDTO> tasks = repository.findAll();
        assertThat(tasks).hasSize(2);
    }

    @Test
    public void shouldReturnCorrectInformationOfFirstTask() {
        TaskListDTO first = repository.findAll().get(0);

        assertThat(first.getId()).isEqualByComparingTo(Tasks.WriteExampleApp.ID);
        assertThat(first.getStatus()).isEqualTo(Tasks.WriteExampleApp.STATUS);
        assertThat(first.getTitle()).isEqualTo(Tasks.WriteExampleApp.TITLE);
    }

    @Test
    public void shouldReturnCorrectInformationOfSecondTask() {
        TaskListDTO second = repository.findAll().get(1);

        assertThat(second.getId()).isEqualByComparingTo(Tasks.WriteLesson.ID);
        assertThat(second.getStatus()).isEqualTo(Tasks.WriteLesson.STATUS);
        assertThat(second.getTitle()).isEqualTo(Tasks.WriteLesson.TITLE);
    }
}
