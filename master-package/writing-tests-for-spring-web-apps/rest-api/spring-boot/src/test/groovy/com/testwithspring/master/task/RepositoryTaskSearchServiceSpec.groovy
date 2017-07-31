package com.testwithspring.master.task

import com.testwithspring.master.UnitTest
import org.junit.experimental.categories.Category
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize

@Category(UnitTest.class)
class RepositoryTaskSearchServiceSpec extends Specification {

    private static final TASK_ID = 1L
    private static final TITLE = 'Write an example test'
    private static final SEARCH_TERM_NO_RESULTS = 'notFound'
    private static final SEARCH_TERM_ONE_RESULT = 'examp'
    private static final STATUS = TaskStatus.OPEN

    def repository = Stub(TaskRepository)
    def service = new RepositoryTaskSearchService(repository)

    def 'Search tasks'() {

        when: 'No tasks is found with the given search term'
        repository.search(SEARCH_TERM_NO_RESULTS) >> []

        and: 'The search is performed'
        def results = service.search(SEARCH_TERM_NO_RESULTS)

        then: 'Should return an empty list'
        results.isEmpty()

        when: 'One task is found with the given search term'
        repository.search(SEARCH_TERM_ONE_RESULT) >> [new TaskListDTO(id: TASK_ID, status: STATUS, title: TITLE)]

        and: 'The search is performed'
        results = service.search(SEARCH_TERM_ONE_RESULT)

        then: 'Should return one task'
        results hasSize(1)

        and: 'Should return a task with the correct information'
        def found = results[0]

        found.id == TASK_ID
        found.title == TITLE
        found.status == STATUS
    }
}
