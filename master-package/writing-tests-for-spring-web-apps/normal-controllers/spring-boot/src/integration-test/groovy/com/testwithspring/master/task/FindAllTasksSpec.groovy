package com.testwithspring.master.task

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import com.github.springtestdbunit.annotation.DbUnitConfiguration
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader
import com.testwithspring.master.IntegrationTest
import com.testwithspring.master.IntegrationTestContext
import com.testwithspring.master.Tasks
import com.testwithspring.master.config.Profiles
import org.junit.experimental.categories.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize

@SpringBootTest(classes = [IntegrationTestContext.class])
@TestExecutionListeners(value = DbUnitTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class FindAllTasksSpec extends Specification {

    @Autowired
    TaskRepository repository

    @DatabaseSetup([
        '/com/testwithspring/master/users.xml',
        '/com/testwithspring/master/no-tasks-and-tags.xml'
    ])
    def 'Find all tasks when the database has no tasks'() {

        def tasks

        when: 'The tasks are queried from the database'
        tasks = repository.findAll()

        then: 'Should return an empty list'
        tasks.isEmpty()
    }

    @DatabaseSetup([
            '/com/testwithspring/master/users.xml',
            '/com/testwithspring/master/tasks.xml'
    ])
    def 'Find all tasks when the database has two tasks'() {

        def tasks

        when: 'The tasks are queried from the database'
        tasks = repository.findAll()

        then: 'Should return two tasks'
        tasks hasSize(2)

        and: 'Should return the information of the first task'
        def first = tasks[0]

        first.id == Tasks.WriteExampleApp.ID
        first.status == Tasks.WriteExampleApp.STATUS
        first.title == Tasks.WriteExampleApp.TITLE

        and: 'Should return the information of the second task'
        def second = tasks[1]

        second.id == Tasks.WriteLesson.ID
        second.status == Tasks.WriteLesson.STATUS
        second.title == Tasks.WriteLesson.TITLE
    }
}
