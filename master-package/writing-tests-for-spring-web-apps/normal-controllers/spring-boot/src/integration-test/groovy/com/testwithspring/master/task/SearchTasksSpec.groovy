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
@DatabaseSetup([
    '/com/testwithspring/master/users.xml',
    '/com/testwithspring/master/tasks.xml'
])
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
class SearchTasksSpec extends Specification {

    @Autowired
    TaskRepository repository

    def 'Search tasks'() {

        def searchResults

        when: 'No tasks is found from the database'
        searchResults = repository.search(Tasks.SEARCH_TERM_NOT_FOUND)

        then: 'Should return an empty list'
        searchResults.isEmpty()

        when: 'One task is found from the database'
        searchResults = repository.search(Tasks.SEARCH_TERM_ONE_MATCH)

        then: 'Should return one task'
        searchResults hasSize(1)

        and: 'Should the information of the found task'
        def task = searchResults[0]

        task.id == Tasks.WriteLesson.ID
        task.status == Tasks.WriteLesson.STATUS
        task.title == Tasks.WriteLesson.TITLE

        when: 'Two tasks are found from the database'
        searchResults = repository.search(Tasks.SEARCH_TERM_TWO_MATCHES)

        then: 'Should return two tasks'
        searchResults hasSize(2)

        and: 'Should return the information of the first task'
        def first = searchResults[0]

        first.id == Tasks.WriteExampleApp.ID
        first.status == Tasks.WriteExampleApp.STATUS
        first.title == Tasks.WriteExampleApp.TITLE

        and: 'Should return the information of the second task'
        def second = searchResults[1]

        second.id == Tasks.WriteLesson.ID
        second.status == Tasks.WriteLesson.STATUS
        second.title == Tasks.WriteLesson.TITLE
    }
}
